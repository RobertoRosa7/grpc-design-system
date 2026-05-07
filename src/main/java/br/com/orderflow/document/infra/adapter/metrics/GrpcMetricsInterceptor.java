package br.com.orderflow.document.infra.adapter.metrics;

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * Interceptor gRPC que registra métricas por método usando Micrometer.
 *
 * <p>Métricas expostas:
 * <ul>
 *   <li>{@code grpc.server.requests.total} — contador por método e status</li>
 *   <li>{@code grpc.server.latency} — timer de latência por método</li>
 * </ul>
 *
 * <p>O timer usa o tag {@code method} com o nome completo do método gRPC
 * e o tag {@code status} com o código gRPC retornado, permitindo correlacionar
 * contagens de erro e latência por operação.
 */
@GrpcGlobalServerInterceptor
public class GrpcMetricsInterceptor implements ServerInterceptor {

  static final String METRIC_REQUESTS = "grpc.server.requests.total";
  static final String METRIC_LATENCY = "grpc.server.latency";
  static final String TAG_METHOD = "method";
  static final String TAG_STATUS = "status";

  private final MeterRegistry meterRegistry;

  public GrpcMetricsInterceptor(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {

    String methodName = call.getMethodDescriptor().getFullMethodName();
    long startNanos = System.nanoTime();

    ServerCall<ReqT, RespT> metricsCall = new SimpleForwardingServerCall<>(call) {
      @Override
      public void close(Status status, Metadata trailers) {
        long durationNanos = System.nanoTime() - startNanos;
        String statusCode = status.getCode().name();

        Counter.builder(METRIC_REQUESTS)
            .tag(TAG_METHOD, methodName)
            .tag(TAG_STATUS, statusCode)
            .register(meterRegistry)
            .increment();

        Timer.builder(METRIC_LATENCY)
            .tag(TAG_METHOD, methodName)
            .tag(TAG_STATUS, statusCode)
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry)
            .record(durationNanos, TimeUnit.NANOSECONDS);

        super.close(status, trailers);
      }
    };

    return next.startCall(metricsCall, headers);
  }
}

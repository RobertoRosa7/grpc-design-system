package br.com.orderflow.document.infra.adapter.grpc;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Chaos Engineering interceptor para o servidor gRPC.
 * Ativado apenas com o profile {@code chaos}.
 * Simula: latencia artificial, UNAVAILABLE, DEADLINE_EXCEEDED, RESOURCE_EXHAUSTED.
 */
@Profile("chaos")
@Configuration
public class ChaosInterceptorConfig {

  private static final Logger log = LoggerFactory.getLogger(ChaosInterceptorConfig.class);

  @Bean
  public ServerInterceptor chaosInterceptor(ChaosProperties props) {
    return new ChaosInterceptor(props);
  }

  @ConfigurationProperties(prefix = "chaos")
  public record ChaosProperties(
      @DefaultValue("true") boolean enabled,
      @DefaultValue("0.3") double latencyProbability,
      @DefaultValue("500") int latencyMinMs,
      @DefaultValue("2000") int latencyMaxMs,
      @DefaultValue("0.1") double failureProbability,
      @DefaultValue("0.05") double timeoutProbability,
      @DefaultValue("0.05") double resourceExhaustedProbability
  ) {}

  static final class ChaosInterceptor implements ServerInterceptor {

    private static final String CHAOS_HEADER = "x-chaos-injected";
    private static final String CHAOS_LATENCY = "latency";
    private static final String CHAOS_FAILURE = "failure";
    private static final String CHAOS_TIMEOUT = "timeout";
    private static final String CHAOS_EXHAUSTED = "resource-exhausted";
    private static final Metadata EMPTY_TRAILERS = new Metadata();

    private final ChaosProperties props;

    ChaosInterceptor(ChaosProperties props) {
      this.props = props;
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {

      if (!props.enabled()) {
        return next.startCall(call, headers);
      }

      final double roll = ThreadLocalRandom.current().nextDouble();
      final String method = call.getMethodDescriptor().getFullMethodName();

      if (roll < props.failureProbability()) {
        log.warn("[CHAOS] Injetando UNAVAILABLE na chamada {}", method);
        headers.put(Metadata.Key.of(CHAOS_HEADER, Metadata.ASCII_STRING_MARSHALLER), CHAOS_FAILURE);
        call.close(Status.UNAVAILABLE.withDescription("[CHAOS] Falha injetada"), EMPTY_TRAILERS);
        return new ServerCall.Listener<>() {};
      }

      if (roll < props.failureProbability() + props.timeoutProbability()) {
        log.warn("[CHAOS] Injetando DEADLINE_EXCEEDED na chamada {}", method);
        headers.put(Metadata.Key.of(CHAOS_HEADER, Metadata.ASCII_STRING_MARSHALLER), CHAOS_TIMEOUT);
        call.close(Status.DEADLINE_EXCEEDED.withDescription("[CHAOS] Timeout injetado"), EMPTY_TRAILERS);
        return new ServerCall.Listener<>() {};
      }

      if (roll < props.failureProbability() + props.timeoutProbability() + props.resourceExhaustedProbability()) {
        log.warn("[CHAOS] Injetando RESOURCE_EXHAUSTED na chamada {}", method);
        headers.put(Metadata.Key.of(CHAOS_HEADER, Metadata.ASCII_STRING_MARSHALLER), CHAOS_EXHAUSTED);
        call.close(Status.RESOURCE_EXHAUSTED.withDescription("[CHAOS] Recurso esgotado"), EMPTY_TRAILERS);
        return new ServerCall.Listener<>() {};
      }

      if (ThreadLocalRandom.current().nextDouble() < props.latencyProbability()) {
        final int delayMs = ThreadLocalRandom.current().nextInt(props.latencyMinMs(), props.latencyMaxMs() + 1);
        log.info("[CHAOS] Injetando latencia de {}ms na chamada {}", delayMs, method);
        headers.put(
            Metadata.Key.of(CHAOS_HEADER, Metadata.ASCII_STRING_MARSHALLER),
            CHAOS_LATENCY + ":" + delayMs + "ms");
        try {
          Thread.sleep(delayMs); // NOSONAR: latencia intencional em Chaos Engineering
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      return next.startCall(new LoggingForwardingCall<>(call), headers);
    }

    private static final class LoggingForwardingCall<ReqT, RespT>
        extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {

      LoggingForwardingCall(ServerCall<ReqT, RespT> delegate) {
        super(delegate);
      }

      @Override
      public void close(Status status, Metadata trailers) {
        if (!status.isOk()) {
          log.debug("[CHAOS] Chamada encerrada com status: {}", status.getCode());
        }
        super.close(status, trailers);
      }
    }
  }
}
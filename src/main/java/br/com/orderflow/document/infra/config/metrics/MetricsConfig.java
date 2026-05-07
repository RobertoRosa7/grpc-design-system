package br.com.orderflow.document.infra.config.metrics;

import br.com.orderflow.document.infra.adapter.metrics.GrpcMetricsInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registra o interceptor de métricas gRPC no contexto Spring.
 *
 * <p>A anotação {@code @GrpcGlobalServerInterceptor} na classe
 * {@link GrpcMetricsInterceptor} já bastaria para o starter do grpc-spring-boot,
 * mas expor como {@code @Bean} explícito garante que o bean seja criado
 * com o {@link MeterRegistry} correto mesmo em contextos de teste.
 */
@Configuration
public class MetricsConfig {

  @Bean
  public GrpcMetricsInterceptor grpcMetricsInterceptor(MeterRegistry meterRegistry) {
    return new GrpcMetricsInterceptor(meterRegistry);
  }
}

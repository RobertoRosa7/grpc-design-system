package br.com.orderflow.document.infra.config.minio;

import io.minio.MinioClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.orderflow.document.infra.config.mongodb.MinioProperties;

/**
 * Configuração da integração com MinIO.
 * <p>
 * Cria o bean {@link MinioClient} a partir das propriedades definidas
 * em {@link MinioProperties} ({@code application.yml}, prefixo {@code minio}).
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }
}

package br.com.orderflow.document.infra.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracao do OpenAPI para a API REST.
 */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI documentServiceOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("OrderFlow Document REST API")
            .description("API REST para geracao e consulta de documentos")
            .version("v1")
            .contact(new Contact()
                .name("OrderFlow")
                .url("https://orderflow.com.br"))
            .license(new License()
                .name("Uso interno")
                .url("https://orderflow.com.br/licensing")));
  }
}

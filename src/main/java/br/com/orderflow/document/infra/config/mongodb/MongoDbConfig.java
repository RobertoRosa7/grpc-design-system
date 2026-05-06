package br.com.orderflow.document.infra.config.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuração explícita da integração com MongoDB.
 * <p>
 * Cria os beans {@link MongoClient} e {@link MongoTemplate} a partir das
 * propriedades do Spring Boot ({@code spring.data.mongodb.*} no
 * {@code application.yml}), tornando a configuração visível e rastreável
 * em vez de depender exclusivamente da auto-configuração do Spring.
 */
@Configuration
public class MongoDbConfig {

    @Bean
    @Primary
    public MongoClient mongoClient(MongoProperties properties) {
        return MongoClients.create(properties.determineUri());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient, MongoProperties properties) {
        String database = properties.getDatabase() != null
                ? properties.getDatabase()
                : "orderflow";
        return new MongoTemplate(mongoClient, database);
    }
}

package br.com.orderflow.document.infra.adapter.mongodb;

import br.com.orderflow.document.domain.model.Document;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper da camada de infraestrutura MongoDB.
 * Responsável por converter entre a entidade de persistência
 * DocumentMongoEntity e a entidade de domínio Document.
 * Centraliza o mapeamento e libera o DocumentMongoRepositoryAdapter
 * de qualquer lógica de conversão.
 */
@Component
public class DocumentMongoMapper {

    /**
     * Converte entidade de domínio para entidade de persistência MongoDB.
     */
    public DocumentMongoEntity toEntity(Document domain) {
        DocumentMongoEntity entity = new DocumentMongoEntity();
        entity.setId(domain.getId());
        entity.setCorrelationId(domain.getCorrelationId());
        entity.setType(domain.getType());
        entity.setStatus(domain.getStatus());
        entity.setStoragePath(domain.getStoragePath());
        entity.setGeneratedAt(domain.getGeneratedAt());
        return entity;
    }

    /**
     * Converte entidade de persistência MongoDB para entidade de domínio.
     */
    public Document toDomain(DocumentMongoEntity entity) {
        Document domain = Document.create(entity.getCorrelationId(), entity.getType());
        domain.setId(entity.getId());

        Optional.ofNullable(entity.getStatus())
                .ifPresent(status -> {
                    switch (status) {
                        case COMPLETED -> domain.markCompleted(entity.getStoragePath());
                        case FAILED -> domain.markFailed();
                        case PROCESSING -> {
                            // PROCESSING já é o estado padrão de Document.create()
                        }
                    }
                });
        return domain;
    }
}

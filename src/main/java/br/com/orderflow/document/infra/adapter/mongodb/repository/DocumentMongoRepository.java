package br.com.orderflow.document.infra.adapter.mongodb.repository;

import br.com.orderflow.document.infra.adapter.mongodb.DocumentMongoRepositoryAdapter;
import br.com.orderflow.document.infra.adapter.mongodb.entity.DocumentMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositório Spring Data MongoDB para a entidade de persistência.
 * <p>
 * Não é exposto diretamente ao domínio. O
 * {@link DocumentMongoRepositoryAdapter}
 * encapsula este repositório e implementa a porta de saída {@link
 * br.com.orderflow.document.domain.port.out.DocumentRepositoryPort}.
 */
public interface DocumentMongoRepository extends MongoRepository<DocumentMongoEntity, String> {
}

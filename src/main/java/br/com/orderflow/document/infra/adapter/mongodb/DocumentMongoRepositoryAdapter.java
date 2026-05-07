package br.com.orderflow.document.infra.adapter.mongodb;

import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.port.out.DocumentRepositoryPort;
import br.com.orderflow.document.infra.adapter.mongodb.constant.MongoConstants;
import br.com.orderflow.document.infra.adapter.mongodb.entity.DocumentMongoEntity;
import br.com.orderflow.document.infra.adapter.mongodb.exception.MongoPersistenceException;
import br.com.orderflow.document.infra.adapter.mongodb.mapper.DocumentMongoMapper;
import br.com.orderflow.document.infra.adapter.mongodb.repository.DocumentMongoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de saída: implementa DocumentRepositoryPort usando MongoDB
 * via Spring Data.
 * Delega a conversão entre o modelo de domínio e a entidade de persistência
 * ao DocumentMongoMapper. Este adaptador contém apenas orquestração.
 *
 * <p>Resiliência aplicada:
 * <ul>
 *   <li>{@code @Retry} — reexecuta em falhas transitórias de rede com MongoDB.</li>
 *   <li>{@code @CircuitBreaker} — abre o circuito quando MongoDB está degradado.</li>
 * </ul>
 */
@Component
public class DocumentMongoRepositoryAdapter implements DocumentRepositoryPort {

  private final DocumentMongoRepository mongoRepository;
  private final DocumentMongoMapper mongoMapper;

  public DocumentMongoRepositoryAdapter(
      DocumentMongoRepository mongoRepository,
      DocumentMongoMapper mongoMapper) {
    this.mongoRepository = mongoRepository;
    this.mongoMapper = mongoMapper;
  }

  @Override
  @Retry(name = "mongodb", fallbackMethod = "saveFallback")
  @CircuitBreaker(name = "mongodb", fallbackMethod = "saveFallback")
  public Document save(Document document) {
    try {
      DocumentMongoEntity entity = mongoMapper.toEntity(document);
      DocumentMongoEntity saved = mongoRepository.save(entity);
      return mongoMapper.toDomain(saved);
    } catch (Exception ex) {
      throw new MongoPersistenceException(MongoConstants.ERROR_PERSISTENCE, ex);
    }
  }

  @Override
  @Retry(name = "mongodb", fallbackMethod = "findByIdFallback")
  @CircuitBreaker(name = "mongodb", fallbackMethod = "findByIdFallback")
  public Optional<Document> findById(String id) {
    try {
      return mongoRepository.findById(id).map(mongoMapper::toDomain);
    } catch (Exception ex) {
      throw new MongoPersistenceException(MongoConstants.ERROR_PERSISTENCE, ex);
    }
  }

  @SuppressWarnings("unused")
  private Document saveFallback(Document document, Exception ex) {
    throw new MongoPersistenceException(MongoConstants.ERROR_PERSISTENCE, ex);
  }

  @SuppressWarnings("unused")
  private Optional<Document> findByIdFallback(String id, Exception ex) {
    throw new MongoPersistenceException(MongoConstants.ERROR_PERSISTENCE, ex);
  }
}

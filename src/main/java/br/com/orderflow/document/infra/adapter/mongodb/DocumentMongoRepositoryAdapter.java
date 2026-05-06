package br.com.orderflow.document.infra.adapter.mongodb;

import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.port.out.DocumentRepositoryPort;
import br.com.orderflow.document.infra.adapter.mongodb.constant.MongoConstants;
import br.com.orderflow.document.infra.adapter.mongodb.entity.DocumentMongoEntity;
import br.com.orderflow.document.infra.adapter.mongodb.exception.MongoPersistenceException;
import br.com.orderflow.document.infra.adapter.mongodb.mapper.DocumentMongoMapper;
import br.com.orderflow.document.infra.adapter.mongodb.repository.DocumentMongoRepository;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de saída: implementa DocumentRepositoryPort usando MongoDB
 * via Spring Data.
 * Delega a conversão entre o modelo de domínio e a entidade de persistência
 * ao DocumentMongoMapper. Este adaptador contém apenas orquestração.
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
  public Optional<Document> findById(String id) {
    try {
      return mongoRepository.findById(id).map(mongoMapper::toDomain);
    } catch (Exception ex) {
      throw new MongoPersistenceException(MongoConstants.ERROR_PERSISTENCE, ex);
    }
  }
}

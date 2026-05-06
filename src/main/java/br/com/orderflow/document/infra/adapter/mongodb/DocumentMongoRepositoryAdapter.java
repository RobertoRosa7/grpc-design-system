package br.com.orderflow.document.infra.adapter.mongodb;

import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.port.out.DocumentRepositoryPort;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de saída: implementa {@link DocumentRepositoryPort} usando MongoDB
 * via Spring Data.
 * <p>
 * Delega a conversão entre o modelo de domínio e a entidade de persistência
 * ao {@link DocumentMongoMapper}. Este adaptador contém apenas orquestração.
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
    DocumentMongoEntity entity = mongoMapper.toEntity(document);
    DocumentMongoEntity saved = mongoRepository.save(entity);
    return mongoMapper.toDomain(saved);
  }

  @Override
  public Optional<Document> findById(String id) {
    return mongoRepository.findById(id).map(mongoMapper::toDomain);
  }
}

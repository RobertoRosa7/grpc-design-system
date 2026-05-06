package br.com.orderflow.document.infra.adapter.mongodb.entity;

import br.com.orderflow.document.domain.model.DocumentStatus;
import br.com.orderflow.document.domain.model.DocumentType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Entidade de persistência MongoDB.
 * <p>
 * Existe apenas na camada do adaptador. O domínio não a conhece.
 * A conversão entre {@link br.com.orderflow.document.domain.model.Document}
 * e esta entidade é responsabilidade do adaptador Mongo.
 */
@Document(collection = "documents")
public class DocumentMongoEntity {

  @Id
  private String id;
  private String correlationId;
  private DocumentType type;
  private DocumentStatus status;
  private String storagePath;
  private Instant generatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public DocumentType getType() {
    return type;
  }

  public void setType(DocumentType type) {
    this.type = type;
  }

  public DocumentStatus getStatus() {
    return status;
  }

  public void setStatus(DocumentStatus status) {
    this.status = status;
  }

  public String getStoragePath() {
    return storagePath;
  }

  public void setStoragePath(String storagePath) {
    this.storagePath = storagePath;
  }

  public Instant getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(Instant generatedAt) {
    this.generatedAt = generatedAt;
  }
}

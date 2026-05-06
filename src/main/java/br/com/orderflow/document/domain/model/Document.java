package br.com.orderflow.document.domain.model;

import java.time.Instant;

/**
 * Entidade central do domínio. Representa um documento PDF gerado pelo serviço.
 * <p>
 * Não possui dependência de framework — qualquer anotação de persistência
 * ou transporte pertence aos adaptadores, não ao domínio.
 */
public class Document {

    private String id;
    private String correlationId;
    private DocumentType type;
    private DocumentStatus status;
    private String storagePath;
    private Instant generatedAt;

    private Document() {
    }

    public static Document create(String correlationId, DocumentType type) {
        Document doc = new Document();
        doc.correlationId = correlationId;
        doc.type = type;
        doc.status = DocumentStatus.PROCESSING;
        doc.generatedAt = Instant.now();
        return doc;
    }

    public void markCompleted(String storagePath) {
        this.storagePath = storagePath;
        this.status = DocumentStatus.COMPLETED;
    }

    public void markFailed() {
        this.status = DocumentStatus.FAILED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public DocumentType getType() {
        return type;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }
}

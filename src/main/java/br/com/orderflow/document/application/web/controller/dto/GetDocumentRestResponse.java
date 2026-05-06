package br.com.orderflow.document.application.web.controller.dto;

import br.com.orderflow.document.domain.model.DocumentStatus;
import br.com.orderflow.document.domain.model.DocumentType;

/**
 * Response REST de consulta de documento.
 */
public record GetDocumentRestResponse(
    String documentId,
    DocumentStatus status,
    DocumentType type,
    String storagePath,
    long generatedAt
) {
}

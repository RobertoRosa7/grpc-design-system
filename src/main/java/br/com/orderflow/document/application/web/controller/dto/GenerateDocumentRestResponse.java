package br.com.orderflow.document.application.web.controller.dto;

import br.com.orderflow.document.domain.model.DocumentStatus;

/**
 * Response REST de geração de documento.
 */
public record GenerateDocumentRestResponse(
        String documentId,
        DocumentStatus status,
        String storagePath,
        long generatedAt) {
}

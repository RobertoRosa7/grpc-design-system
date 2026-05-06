package br.com.orderflow.document.domain.dto;

import br.com.orderflow.document.domain.model.DocumentStatus;
import br.com.orderflow.document.domain.model.DocumentType;

/**
 * DTO de saída para consulta de documento existente.
 */
public record GetDocumentResponseDto(
        String documentId,
        DocumentStatus status,
        DocumentType type,
        String storagePath,
        long generatedAt) {
}

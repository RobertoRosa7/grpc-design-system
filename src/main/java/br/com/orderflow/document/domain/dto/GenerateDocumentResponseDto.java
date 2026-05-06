package br.com.orderflow.document.domain.dto;

import br.com.orderflow.document.domain.model.DocumentStatus;

/**
 * DTO de saída após a geração de um documento PDF.
 * <p>
 * Retornado pelo caso de uso e convertido para o formato
 * do adaptador de saída (proto, JSON, etc.).
 */
public record GenerateDocumentResponseDto(
        String documentId,
        DocumentStatus status,
        String storagePath,
        long generatedAt) {
}

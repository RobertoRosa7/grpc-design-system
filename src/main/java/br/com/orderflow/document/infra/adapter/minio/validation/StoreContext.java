package br.com.orderflow.document.infra.adapter.minio.validation;

/**
 * Contexto de validação para operação de armazenamento.
 */
public record StoreContext(
    String documentId,
    byte[] content,
    String bucket
) {
}

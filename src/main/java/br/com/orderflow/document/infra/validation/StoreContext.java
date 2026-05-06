package br.com.orderflow.document.infra.validation;

/**
 * Contexto de validação para operação de armazenamento.
 */
public record StoreContext(
    String documentId,
    byte[] content,
    String bucket
) {
}

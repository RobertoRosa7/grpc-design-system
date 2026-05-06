package br.com.orderflow.document.domain.model;

/**
 * Ciclo de vida de processamento de um documento.
 * Reflete diretamente o enum {@code DocumentStatus} definido no contrato proto,
 * mas existe de forma independente no domínio.
 */
public enum DocumentStatus {
    PROCESSING,
    COMPLETED,
    FAILED
}

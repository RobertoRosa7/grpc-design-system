package br.com.orderflow.document.domain.model;

/**
 * Tipos de documento suportados pelo serviço.
 * Mapeado a partir do enum {@code DocumentType} do contrato proto pelo
 * adaptador gRPC.
 */
public enum DocumentType {
    INVOICE,
    CONTRACT,
    REPORT
}

package br.com.orderflow.document.domain.dto;

import br.com.orderflow.document.domain.model.DocumentType;

import java.util.List;

/**
 * DTO de entrada para o caso de uso de geração de documento.
 * <p>
 * Representa os dados recebidos de qualquer adaptador de entrada
 * (gRPC, REST, CLI) antes da conversão para o modelo de domínio.
 */
public record GenerateDocumentRequestDto(
        String correlationId,
        DocumentType type,
        // Campos de nota fiscal
        String customerId,
        String orderId,
        List<LineItemDto> items,
        double totalAmount,
        String currency,
        // Campos de contrato
        String partyA,
        String partyB,
        String contractContent,
        String validUntil,
        // Campos de relatório
        String reportTitle,
        String period,
        String reportContent
) {

    /**
     * DTO de item de linha para nota fiscal.
     */
    public record LineItemDto(String description, int quantity, double unitPrice) {}
}
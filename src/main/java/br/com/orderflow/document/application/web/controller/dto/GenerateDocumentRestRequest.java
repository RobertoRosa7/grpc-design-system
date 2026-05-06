package br.com.orderflow.document.application.web.controller.dto;

import br.com.orderflow.document.domain.dto.GenerateDocumentRequestDto;
import br.com.orderflow.document.domain.model.DocumentType;

import java.util.List;

/**
 * Request REST para geração de documento.
 */
public record GenerateDocumentRestRequest(
    String correlationId,
    DocumentType type,
    String customerId,
    String orderId,
    List<GenerateDocumentRequestDto.LineItemDto> items,
    double totalAmount,
    String currency,
    String partyA,
    String partyB,
    String contractContent,
    String validUntil,
    String reportTitle,
    String period,
    String reportContent
) {
}

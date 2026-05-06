package br.com.orderflow.document.domain.mapper;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.dto.GenerateDocumentRequestDto;
import br.com.orderflow.document.domain.dto.GenerateDocumentResponseDto;
import br.com.orderflow.document.domain.dto.GetDocumentResponseDto;
import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.model.DocumentPayload.LineItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Mapper da camada de aplicação.
 * Responsável por converter entre DTOs (objetos de fronteira) e o modelo
 * de domínio. Centraliza o mapeamento, mantendo o caso de uso e os adaptadores
 * livres de lógica de conversão.
 * Fluxo de entrada: GenerateDocumentRequestDto para DocumentPayload.
 * Fluxo de saída: Document para GenerateDocumentResponseDto.
 * Fluxo de saída: Document para GetDocumentResponseDto.
 */
@Component
public class DocumentMapper {

    /**
     * Converte DTO de entrada para o value object interno do domínio.
     */
    public DocumentPayload toPayload(GenerateDocumentRequestDto dto) {
        List<LineItem> items = dto.items() == null
                ? Collections.emptyList()
                : dto.items().stream()
                        .map(i -> new LineItem(i.description(), i.quantity(), i.unitPrice()))
                        .toList();

        return DocumentPayload.builder()
                .correlationId(dto.correlationId())
                .type(dto.type())
                .customerId(dto.customerId())
                .orderId(dto.orderId())
                .items(items)
                .totalAmount(dto.totalAmount())
                .currency(dto.currency())
                .partyA(dto.partyA())
                .partyB(dto.partyB())
                .contractContent(dto.contractContent())
                .validUntil(dto.validUntil())
                .reportTitle(dto.reportTitle())
                .period(dto.period())
                .reportContent(dto.reportContent())
                .build();
    }

    /**
     * Converte a entidade de domínio para o DTO de resposta de geração.
     */
    public GenerateDocumentResponseDto toGenerateResponse(Document document) {
        return new GenerateDocumentResponseDto(
                document.getId(),
                document.getStatus(),
                document.getStoragePath(),
                document.getGeneratedAt().toEpochMilli());
    }

    /**
     * Converte a entidade de domínio para o DTO de resposta de consulta.
     */
    public GetDocumentResponseDto toGetResponse(Document document) {
        return new GetDocumentResponseDto(
                document.getId(),
                document.getStatus(),
                document.getType(),
                document.getStoragePath() != null ? document.getStoragePath() : DomainConstants.EMPTY_VALUE,
                document.getGeneratedAt().toEpochMilli());
    }
}

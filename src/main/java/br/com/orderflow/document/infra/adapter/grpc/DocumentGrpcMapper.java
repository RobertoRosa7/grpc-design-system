package br.com.orderflow.document.infra.adapter.grpc;

import br.com.orderflow.document.domain.dto.GenerateDocumentRequestDto;
import br.com.orderflow.document.domain.dto.GenerateDocumentRequestDto.LineItemDto;
import br.com.orderflow.document.domain.dto.GenerateDocumentResponseDto;
import br.com.orderflow.document.domain.dto.GetDocumentResponseDto;
import br.com.orderflow.document.domain.model.DocumentStatus;
import br.com.orderflow.document.domain.model.DocumentType;
import br.com.orderflow.document.v1.GenerateDocumentRequest;
import br.com.orderflow.document.v1.GenerateDocumentResponse;
import br.com.orderflow.document.v1.GetDocumentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper da camada de infraestrutura gRPC.
 * <p>
 * Responsável exclusivamente pela conversão entre mensagens Protobuf
 * e os DTOs da camada de domínio. Não contém lógica de negócio.
 * <p>
 * Fluxo de entrada: {@link GenerateDocumentRequest} (proto) →
 * {@link GenerateDocumentRequestDto}
 * Fluxo de saída: {@link GenerateDocumentResponseDto} →
 * {@link GenerateDocumentResponse} (proto)
 * {@link GetDocumentResponseDto} → {@link GetDocumentResponse} (proto)
 */
@Component
public class DocumentGrpcMapper {

    /**
     * Converte a requisição proto de geração para o DTO de entrada.
     */
    public GenerateDocumentRequestDto toRequestDto(GenerateDocumentRequest request) {
        DocumentType type = fromProtoType(request.getType());

        return switch (request.getPayloadCase()) {
            case INVOICE -> {
                var inv = request.getInvoice();
                List<LineItemDto> items = inv.getItemsList().stream()
                        .map(i -> new LineItemDto(i.getDescription(), i.getQuantity(), i.getUnitPrice()))
                        .toList();
                yield new GenerateDocumentRequestDto(
                        request.getCorrelationId(), type,
                        inv.getCustomerId(), inv.getOrderId(), items, inv.getTotalAmount(), inv.getCurrency(),
                        null, null, null, null,
                        null, null, null);
            }
            case CONTRACT -> {
                var c = request.getContract();
                yield new GenerateDocumentRequestDto(
                        request.getCorrelationId(), type,
                        null, null, List.of(), 0.0, null,
                        c.getPartyA(), c.getPartyB(), c.getContent(), c.getValidUntil(),
                        null, null, null);
            }
            case REPORT -> {
                var r = request.getReport();
                yield new GenerateDocumentRequestDto(
                        request.getCorrelationId(), type,
                        null, null, List.of(), 0.0, null,
                        null, null, null, null,
                        r.getTitle(), r.getPeriod(), r.getContent());
            }
            default -> throw new IllegalArgumentException(
                    "Payload não informado para o tipo: " + request.getType());
        };
    }

    /**
     * Converte o DTO de resposta de geração para a mensagem proto.
     */
    public GenerateDocumentResponse toGenerateResponse(GenerateDocumentResponseDto dto) {
        return GenerateDocumentResponse.newBuilder()
                .setDocumentId(dto.documentId())
                .setStatus(toProtoStatus(dto.status()))
                .setStoragePath(dto.storagePath() != null ? dto.storagePath() : "")
                .setGeneratedAt(dto.generatedAt())
                .build();
    }

    /**
     * Converte o DTO de resposta de consulta para a mensagem proto.
     */
    public GetDocumentResponse toGetResponse(GetDocumentResponseDto dto) {
        return GetDocumentResponse.newBuilder()
                .setDocumentId(dto.documentId())
                .setStatus(toProtoStatus(dto.status()))
                .setType(toProtoType(dto.type()))
                .setStoragePath(dto.storagePath())
                .setGeneratedAt(dto.generatedAt())
                .build();
    }

    // -------------------------------------------------------------------------
    // Conversões de enum proto ↔ domínio
    // -------------------------------------------------------------------------

    private DocumentType fromProtoType(br.com.orderflow.document.v1.DocumentType protoType) {
        return switch (protoType) {
            case DOCUMENT_TYPE_INVOICE -> DocumentType.INVOICE;
            case DOCUMENT_TYPE_CONTRACT -> DocumentType.CONTRACT;
            case DOCUMENT_TYPE_REPORT -> DocumentType.REPORT;
            default -> throw new IllegalArgumentException("Tipo de documento inválido: " + protoType);
        };
    }

    private br.com.orderflow.document.v1.DocumentStatus toProtoStatus(DocumentStatus status) {
        return switch (status) {
            case PROCESSING -> br.com.orderflow.document.v1.DocumentStatus.DOCUMENT_STATUS_PROCESSING;
            case COMPLETED -> br.com.orderflow.document.v1.DocumentStatus.DOCUMENT_STATUS_COMPLETED;
            case FAILED -> br.com.orderflow.document.v1.DocumentStatus.DOCUMENT_STATUS_FAILED;
        };
    }

    private br.com.orderflow.document.v1.DocumentType toProtoType(DocumentType type) {
        return switch (type) {
            case INVOICE -> br.com.orderflow.document.v1.DocumentType.DOCUMENT_TYPE_INVOICE;
            case CONTRACT -> br.com.orderflow.document.v1.DocumentType.DOCUMENT_TYPE_CONTRACT;
            case REPORT -> br.com.orderflow.document.v1.DocumentType.DOCUMENT_TYPE_REPORT;
        };
    }
}

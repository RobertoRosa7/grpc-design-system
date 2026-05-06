package br.com.orderflow.document.application.grpc.server;

import br.com.orderflow.document.application.exception.ApplicationValidationException;
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
 * Mapper entre mensagens gRPC e DTOs da aplicação.
 */
@Component
public class ApplicationGrpcMapper {

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
        var contract = request.getContract();
        yield new GenerateDocumentRequestDto(
            request.getCorrelationId(), type,
            null, null, List.of(), 0.0, null,
            contract.getPartyA(), contract.getPartyB(), contract.getContent(), contract.getValidUntil(),
            null, null, null);
      }
      case REPORT -> {
        var report = request.getReport();
        yield new GenerateDocumentRequestDto(
            request.getCorrelationId(), type,
            null, null, List.of(), 0.0, null,
            null, null, null, null,
            report.getTitle(), report.getPeriod(), report.getContent());
      }
      default -> throw new ApplicationValidationException(
          "Payload não informado para o tipo: " + request.getType(),
          null);
    };
  }

  public GenerateDocumentResponse toGenerateResponse(GenerateDocumentResponseDto dto) {
    return GenerateDocumentResponse.newBuilder()
        .setDocumentId(dto.documentId())
        .setStatus(toProtoStatus(dto.status()))
        .setStoragePath(dto.storagePath() != null ? dto.storagePath() : "")
        .setGeneratedAt(dto.generatedAt())
        .build();
  }

  public GetDocumentResponse toGetResponse(GetDocumentResponseDto dto) {
    return GetDocumentResponse.newBuilder()
        .setDocumentId(dto.documentId())
        .setStatus(toProtoStatus(dto.status()))
        .setType(toProtoType(dto.type()))
        .setStoragePath(dto.storagePath() != null ? dto.storagePath() : "")
        .setGeneratedAt(dto.generatedAt())
        .build();
  }

  private DocumentType fromProtoType(br.com.orderflow.document.v1.DocumentType protoType) {
    return switch (protoType) {
      case DOCUMENT_TYPE_INVOICE -> DocumentType.INVOICE;
      case DOCUMENT_TYPE_CONTRACT -> DocumentType.CONTRACT;
      case DOCUMENT_TYPE_REPORT -> DocumentType.REPORT;
      default -> throw new ApplicationValidationException("Tipo de documento inválido: " + protoType, null);
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

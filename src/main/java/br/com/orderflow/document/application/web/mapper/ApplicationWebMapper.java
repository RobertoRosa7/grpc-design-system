package br.com.orderflow.document.application.web.mapper;

import br.com.orderflow.document.application.grpc.server.mapper.DomainDocumentMapper;
import br.com.orderflow.document.application.web.controller.dto.GenerateDocumentRestRequest;
import br.com.orderflow.document.application.web.controller.dto.GenerateDocumentRestResponse;
import br.com.orderflow.document.application.web.controller.dto.GetDocumentRestResponse;
import br.com.orderflow.document.domain.dto.GenerateDocumentRequestDto;
import br.com.orderflow.document.domain.dto.GenerateDocumentResponseDto;
import br.com.orderflow.document.domain.dto.GetDocumentResponseDto;
import br.com.orderflow.document.domain.model.DocumentPayload;
import org.springframework.stereotype.Component;

/**
 * Mapper da camada de aplicação.
 */
@Component
public class ApplicationWebMapper {

  private final DomainDocumentMapper domainMapper;

  public ApplicationWebMapper(DomainDocumentMapper domainMapper) {
    this.domainMapper = domainMapper;
  }

  public GenerateDocumentRequestDto toRequestDto(GenerateDocumentRestRequest request) {
    return new GenerateDocumentRequestDto(
        request.correlationId(),
        request.type(),
        request.customerId(),
        request.orderId(),
        request.items(),
        request.totalAmount(),
        request.currency(),
        request.partyA(),
        request.partyB(),
        request.contractContent(),
        request.validUntil(),
        request.reportTitle(),
        request.period(),
        request.reportContent());
  }

  public DocumentPayload toPayload(GenerateDocumentRequestDto dto) {
    return domainMapper.toPayload(dto);
  }

  public GenerateDocumentRestResponse toRestGenerateResponse(GenerateDocumentResponseDto dto) {
    return new GenerateDocumentRestResponse(dto.documentId(), dto.status(), dto.storagePath(), dto.generatedAt());
  }

  public GetDocumentRestResponse toRestGetResponse(GetDocumentResponseDto dto) {
    return new GetDocumentRestResponse(dto.documentId(), dto.status(), dto.type(), dto.storagePath(), dto.generatedAt());
  }
}

package br.com.orderflow.document.application.controller;

import br.com.orderflow.document.application.constant.ApplicationConstants;
import br.com.orderflow.document.application.controller.dto.GenerateDocumentRestRequest;
import br.com.orderflow.document.application.controller.dto.GenerateDocumentRestResponse;
import br.com.orderflow.document.application.controller.dto.GetDocumentRestResponse;
import br.com.orderflow.document.application.exception.ApplicationNotFoundException;
import br.com.orderflow.document.application.mapper.ApplicationDocumentMapper;
import br.com.orderflow.document.domain.mapper.DocumentMapper;
import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.port.in.GeneratePdfPort;
import br.com.orderflow.document.domain.port.in.GetDocumentPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST da camada de aplicação.
 */
@RestController
@RequestMapping(ApplicationConstants.REST_BASE_PATH)
public class DocumentController {

  private final GeneratePdfPort generatePdfPort;
  private final GetDocumentPort getDocumentPort;
  private final ApplicationDocumentMapper applicationMapper;
  private final DocumentMapper domainMapper;

  public DocumentController(
      GeneratePdfPort generatePdfPort,
      GetDocumentPort getDocumentPort,
      ApplicationDocumentMapper applicationMapper,
      DocumentMapper domainMapper) {
    this.generatePdfPort = generatePdfPort;
    this.getDocumentPort = getDocumentPort;
    this.applicationMapper = applicationMapper;
    this.domainMapper = domainMapper;
  }

  @PostMapping
  public ResponseEntity<GenerateDocumentRestResponse> generate(
      @RequestHeader(name = ApplicationConstants.HEADER_CORRELATION_ID, required = false) String correlationId,
      @RequestBody GenerateDocumentRestRequest request) {
    var effectiveRequest = correlationId == null || correlationId.isBlank()
        ? request
        : new GenerateDocumentRestRequest(
            correlationId,
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

    var requestDto = applicationMapper.toRequestDto(effectiveRequest);
    DocumentPayload payload = applicationMapper.toPayload(requestDto);
    Document generated = generatePdfPort.execute(payload);
    var responseDto = domainMapper.toGenerateResponse(generated);
    return ResponseEntity.ok(applicationMapper.toRestGenerateResponse(responseDto));
  }

  @GetMapping(ApplicationConstants.REST_PATH_ID)
  public ResponseEntity<GetDocumentRestResponse> getById(@PathVariable String documentId) {
    Document found = getDocumentPort.execute(documentId)
        .orElseThrow(() -> new ApplicationNotFoundException(
            ApplicationConstants.GRPC_ERROR_NOT_FOUND_PREFIX + documentId,
            null));
    var responseDto = domainMapper.toGetResponse(found);
    return ResponseEntity.ok(applicationMapper.toRestGetResponse(responseDto));
  }
}

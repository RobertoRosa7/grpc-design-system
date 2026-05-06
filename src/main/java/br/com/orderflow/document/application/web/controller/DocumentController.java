package br.com.orderflow.document.application.web.controller;

import br.com.orderflow.document.application.grpc.server.mapper.DomainDocumentMapper;
import br.com.orderflow.document.application.web.constant.WebConstants;
import br.com.orderflow.document.application.web.controller.dto.GenerateDocumentRestRequest;
import br.com.orderflow.document.application.web.controller.dto.GenerateDocumentRestResponse;
import br.com.orderflow.document.application.web.controller.dto.GetDocumentRestResponse;
import br.com.orderflow.document.application.web.controller.exception.ApplicationWebNotFoundException;
import br.com.orderflow.document.application.web.mapper.ApplicationWebMapper;
import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.port.in.GeneratePdfPort;
import br.com.orderflow.document.domain.port.in.GetDocumentPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(WebConstants.REST_BASE_PATH)
public class DocumentController {

  private final GeneratePdfPort generatePdfPort;
  private final GetDocumentPort getDocumentPort;
  private final ApplicationWebMapper applicationMapper;
  private final DomainDocumentMapper domainMapper;

  public DocumentController(
      GeneratePdfPort generatePdfPort,
      GetDocumentPort getDocumentPort,
      ApplicationWebMapper applicationMapper,
      DomainDocumentMapper domainMapper) {
    this.generatePdfPort = generatePdfPort;
    this.getDocumentPort = getDocumentPort;
    this.applicationMapper = applicationMapper;
    this.domainMapper = domainMapper;
  }

  @PostMapping
    @Operation(summary = "Gerar documento", description = "Gera um documento PDF para o payload informado")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Documento gerado com sucesso",
        content = @Content(schema = @Schema(implementation = GenerateDocumentRestResponse.class))),
      @ApiResponse(responseCode = "400", description = "Requisicao invalida"),
      @ApiResponse(responseCode = "500", description = "Erro interno")
    })
  public ResponseEntity<GenerateDocumentRestResponse> generate(
      @Parameter(description = "Correlation id da requisicao", required = false)
      @RequestHeader(name = WebConstants.HEADER_CORRELATION_ID, required = false) String correlationId,
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

  @GetMapping(WebConstants.REST_PATH_ID)
    @Operation(summary = "Consultar documento", description = "Consulta documento por identificador")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Documento encontrado",
        content = @Content(schema = @Schema(implementation = GetDocumentRestResponse.class))),
      @ApiResponse(responseCode = "404", description = "Documento nao encontrado"),
      @ApiResponse(responseCode = "500", description = "Erro interno")
    })
  public ResponseEntity<GetDocumentRestResponse> getById(@PathVariable String documentId) {
    Document found = getDocumentPort.execute(documentId)
        .orElseThrow(() -> new ApplicationWebNotFoundException(
            WebConstants.GRPC_ERROR_NOT_FOUND_PREFIX + documentId,
            null));
    var responseDto = domainMapper.toGetResponse(found);
    return ResponseEntity.ok(applicationMapper.toRestGetResponse(responseDto));
  }
}

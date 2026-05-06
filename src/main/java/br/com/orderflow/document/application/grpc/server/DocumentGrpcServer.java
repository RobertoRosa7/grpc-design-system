package br.com.orderflow.document.application.grpc.server;

import br.com.orderflow.document.application.constant.ApplicationConstants;
import br.com.orderflow.document.application.exception.ApplicationNotFoundException;
import br.com.orderflow.document.application.exception.ApplicationValidationException;
import br.com.orderflow.document.domain.dto.GenerateDocumentResponseDto;
import br.com.orderflow.document.domain.dto.GetDocumentResponseDto;
import br.com.orderflow.document.domain.exception.DomainException;
import br.com.orderflow.document.domain.mapper.DocumentMapper;
import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.port.in.GeneratePdfPort;
import br.com.orderflow.document.domain.port.in.GetDocumentPort;
import br.com.orderflow.document.v1.DocumentServiceGrpc;
import br.com.orderflow.document.v1.GenerateDocumentRequest;
import br.com.orderflow.document.v1.GenerateDocumentResponse;
import br.com.orderflow.document.v1.GetDocumentRequest;
import br.com.orderflow.document.v1.GetDocumentResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Server gRPC da camada de aplicação.
 */
@GrpcService
public class DocumentGrpcServer extends DocumentServiceGrpc.DocumentServiceImplBase {

  private static final Logger log = LoggerFactory.getLogger(DocumentGrpcServer.class);

  private final GeneratePdfPort generatePdfPort;
  private final GetDocumentPort getDocumentPort;
  private final ApplicationGrpcMapper grpcMapper;
  private final DocumentMapper documentMapper;

  public DocumentGrpcServer(
      GeneratePdfPort generatePdfPort,
      GetDocumentPort getDocumentPort,
      ApplicationGrpcMapper grpcMapper,
      DocumentMapper documentMapper) {
    this.generatePdfPort = generatePdfPort;
    this.getDocumentPort = getDocumentPort;
    this.grpcMapper = grpcMapper;
    this.documentMapper = documentMapper;
  }

  @Override
  public void generateDocument(
      GenerateDocumentRequest request,
      StreamObserver<GenerateDocumentResponse> responseObserver) {
    try {
      var requestDto = grpcMapper.toRequestDto(request);
      DocumentPayload payload = documentMapper.toPayload(requestDto);
      Document generated = generatePdfPort.execute(payload);
      GenerateDocumentResponseDto responseDto = documentMapper.toGenerateResponse(generated);
      responseObserver.onNext(grpcMapper.toGenerateResponse(responseDto));
      responseObserver.onCompleted();
    } catch (DomainException ex) {
      throwGrpcError(responseObserver, new ApplicationValidationException(ex.getMessage(), ex));
    } catch (ApplicationValidationException ex) {
      throwGrpcError(responseObserver, ex);
    } catch (Exception ex) {
      throwGrpcError(responseObserver, ex);
    }
  }

  @Override
  public void getDocument(
      GetDocumentRequest request,
      StreamObserver<GetDocumentResponse> responseObserver) {
    try {
      Optional<Document> found = getDocumentPort.execute(request.getDocumentId());
      Document document = found.orElseThrow(() ->
          new ApplicationNotFoundException(
              ApplicationConstants.GRPC_ERROR_NOT_FOUND_PREFIX + request.getDocumentId(),
              null));
      GetDocumentResponseDto responseDto = documentMapper.toGetResponse(document);
      responseObserver.onNext(grpcMapper.toGetResponse(responseDto));
      responseObserver.onCompleted();
    } catch (ApplicationNotFoundException ex) {
      responseObserver.onError(Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException());
    } catch (ApplicationValidationException ex) {
      throwGrpcError(responseObserver, ex);
    } catch (DomainException ex) {
      throwGrpcError(responseObserver, new ApplicationValidationException(ex.getMessage(), ex));
    } catch (Exception ex) {
      throwGrpcError(responseObserver, ex);
    }
  }

  private void throwGrpcError(StreamObserver<?> responseObserver, Exception ex) {
    Status status = resolveStatus(ex);
    String message = resolveMessage(ex);
    log.error("Falha na operação gRPC", ex);
    responseObserver.onError(status.withDescription(message).asRuntimeException());
  }

  private Status resolveStatus(Exception ex) {
    return ex instanceof ApplicationValidationException
        ? Status.INVALID_ARGUMENT
        : Status.INTERNAL;
  }

  private String resolveMessage(Exception ex) {
    return ex instanceof ApplicationValidationException
        ? ex.getMessage()
        : ApplicationConstants.GRPC_ERROR_INTERNAL;
  }
}

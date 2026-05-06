package br.com.orderflow.document.application.grpc.client;

import br.com.orderflow.document.application.grpc.client.exception.ApplicationGrpcClientException;
import br.com.orderflow.document.application.web.constant.WebConstants;
import br.com.orderflow.document.v1.DocumentServiceGrpc;
import br.com.orderflow.document.v1.GenerateDocumentRequest;
import br.com.orderflow.document.v1.GenerateDocumentResponse;
import br.com.orderflow.document.v1.GetDocumentRequest;
import br.com.orderflow.document.v1.GetDocumentResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * Client gRPC da camada de aplicação.
 */
@Component
public class DocumentGrpcClient {

  @GrpcClient("document-service")
  private DocumentServiceGrpc.DocumentServiceBlockingStub client;

  public GenerateDocumentResponse generateDocument(GenerateDocumentRequest request) {
    try {
      return client.generateDocument(request);
    } catch (Exception ex) {
      throw new ApplicationGrpcClientException(WebConstants.GRPC_ERROR_INTERNAL, ex);
    }
  }

  public GetDocumentResponse getDocument(GetDocumentRequest request) {
    try {
      return client.getDocument(request);
    } catch (Exception ex) {
      throw new ApplicationGrpcClientException(WebConstants.GRPC_ERROR_INTERNAL, ex);
    }
  }
}

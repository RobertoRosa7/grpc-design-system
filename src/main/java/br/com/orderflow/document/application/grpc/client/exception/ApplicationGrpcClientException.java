package br.com.orderflow.document.application.grpc.client.exception;

/**
 * Exceção base do client gRPC.
 */
public class ApplicationGrpcClientException extends RuntimeException {

  public ApplicationGrpcClientException(String message) {
    super(message);
  }

  public ApplicationGrpcClientException(String message, Throwable cause) {
    super(message, cause);
  }
}

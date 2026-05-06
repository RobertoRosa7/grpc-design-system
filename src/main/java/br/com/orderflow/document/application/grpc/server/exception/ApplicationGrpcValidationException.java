package br.com.orderflow.document.application.grpc.server.exception;

/**
 * Exceção de validação no server gRPC.
 */
public class ApplicationGrpcValidationException extends RuntimeException {

    public ApplicationGrpcValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

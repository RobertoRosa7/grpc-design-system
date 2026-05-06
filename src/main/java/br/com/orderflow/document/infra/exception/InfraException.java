package br.com.orderflow.document.infra.exception;

/**
 * Exceção base da infraestrutura.
 */
public class InfraException extends RuntimeException {

    public InfraException(String message) {
        super(message);
    }

    public InfraException(String message, Throwable cause) {
        super(message, cause);
    }
}

package br.com.orderflow.document.infra.adapter.minio.exception;

/**
 * Exceção base da infraestrutura.
 */
public class MinioException extends RuntimeException {

    public MinioException(String message) {
        super(message);
    }

    public MinioException(String message, Throwable cause) {
        super(message, cause);
    }
}

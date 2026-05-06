package br.com.orderflow.document.infra.adapter.minio.exception;

/**
 * Exceção para falhas de storage.
 */
public class MinioStorageException extends MinioException {

  public MinioStorageException(String message) {
    super(message);
  }

  public MinioStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}

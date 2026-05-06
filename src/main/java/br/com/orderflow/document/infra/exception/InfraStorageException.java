package br.com.orderflow.document.infra.exception;

/**
 * Exceção para falhas de storage.
 */
public class InfraStorageException extends InfraException {

  public InfraStorageException(String message) {
    super(message);
  }

  public InfraStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}

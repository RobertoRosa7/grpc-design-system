package br.com.orderflow.document.application.exception;

/**
 * Exceção base da camada de aplicação.
 */
public class ApplicationException extends RuntimeException {

  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}

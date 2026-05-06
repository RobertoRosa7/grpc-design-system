package br.com.orderflow.document.application.exception;

/**
 * Exceção para recursos não encontrados na camada de aplicação.
 */
public class ApplicationNotFoundException extends ApplicationException {

  public ApplicationNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}

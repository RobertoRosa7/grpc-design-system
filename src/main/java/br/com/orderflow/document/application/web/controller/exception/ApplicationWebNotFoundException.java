package br.com.orderflow.document.application.web.controller.exception;

/**
 * Exceção para recursos não encontrados na camada de aplicação.
 */
public class ApplicationWebNotFoundException extends ApplicationWebException {

  public ApplicationWebNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}

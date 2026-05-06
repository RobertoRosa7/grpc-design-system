package br.com.orderflow.document.application.web.controller.exception;

/**
 * Exceção base da camada web.
 */
public class ApplicationWebException extends RuntimeException {

  public ApplicationWebException(String message, Throwable cause) {
    super(message, cause);
  }
}

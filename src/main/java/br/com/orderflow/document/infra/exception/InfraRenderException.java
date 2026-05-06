package br.com.orderflow.document.infra.exception;

/**
 * Exceção para falhas de renderização de PDF.
 */
public class InfraRenderException extends InfraException {

  public InfraRenderException(String message, Throwable cause) {
    super(message, cause);
  }
}

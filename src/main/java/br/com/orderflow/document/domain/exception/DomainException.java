package br.com.orderflow.document.domain.exception;

/**
 * Exceção base da camada de domínio.
 */
public class DomainException extends RuntimeException {

  public DomainException(String message) {
    super(message);
  }

  public DomainException(String message, Throwable cause) {
    super(message, cause);
  }
}

package br.com.orderflow.document.domain.exception;

/**
 * Exceção quando um recurso de domínio não é encontrado.
 */
public class DomainNotFoundException extends DomainException {

  public DomainNotFoundException(String message) {
    super(message);
  }
}

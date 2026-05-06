package br.com.orderflow.document.infra.exception;

/**
 * Exceção para falhas de persistência.
 */
public class InfraPersistenceException extends InfraException {

  public InfraPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}

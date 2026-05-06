package br.com.orderflow.document.infra.adapter.mongodb.exception;

/**
 * Exceção para falhas de persistência.
 */
public class MongoPersistenceException extends RuntimeException {

  public MongoPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}

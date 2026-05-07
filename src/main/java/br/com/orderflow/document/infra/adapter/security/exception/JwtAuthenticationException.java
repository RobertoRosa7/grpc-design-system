package br.com.orderflow.document.infra.adapter.security.exception;

/**
 * Erro de autenticacao JWT.
 */
public class JwtAuthenticationException extends RuntimeException {

  public JwtAuthenticationException(String message) {
    super(message);
  }

  public JwtAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}

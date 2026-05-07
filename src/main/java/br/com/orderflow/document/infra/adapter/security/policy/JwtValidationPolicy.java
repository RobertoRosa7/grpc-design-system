package br.com.orderflow.document.infra.adapter.security.policy;

import br.com.orderflow.document.infra.adapter.security.validation.JwtValidationContext;

/**
 * Contrato para politicas de validacao de claims JWT.
 */
public interface JwtValidationPolicy {

  void validate(JwtValidationContext context);
}

package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_INVALID_ISSUER;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.validation.JwtValidationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Valida a claim de emissor (iss) quando configurada.
 */
@Component
@Order(40)
public class IssuerPolicy implements JwtValidationPolicy {

  @Override
  public void validate(JwtValidationContext context) {
    String expectedIssuer = context.properties().getIssuer();
    if (expectedIssuer == null || expectedIssuer.isBlank()) {
      return;
    }

    String issuer = context.claims().getIssuer();
    if (!expectedIssuer.equals(issuer)) {
      throw new JwtAuthenticationException(MSG_INVALID_ISSUER);
    }
  }
}

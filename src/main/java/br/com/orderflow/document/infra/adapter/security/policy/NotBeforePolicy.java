package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_TOKEN_NOT_YET_VALID;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.validation.JwtValidationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Valida a claim de inicio de validade (nbf).
 */
@Component
@Order(20)
public class NotBeforePolicy implements JwtValidationPolicy {

  @Override
  public void validate(JwtValidationContext context) {
    Date notBefore = context.claims().getNotBeforeTime();
    if (notBefore != null && context.now().plusSeconds(context.skewSeconds()).isBefore(notBefore.toInstant())) {
      throw new JwtAuthenticationException(MSG_TOKEN_NOT_YET_VALID);
    }
  }
}

package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_SUBJECT_MISSING;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.validation.JwtValidationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Valida a claim de subject (sub).
 */
@Component
@Order(30)
public class SubjectPolicy implements JwtValidationPolicy {

  @Override
  public void validate(JwtValidationContext context) {
    String subject = context.claims().getSubject();
    if (subject == null || subject.isBlank()) {
      throw new JwtAuthenticationException(MSG_SUBJECT_MISSING);
    }
  }
}

package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_INVALID_AUDIENCE;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.validation.JwtValidationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Valida a claim de audiencia (aud) quando configurada.
 */
@Component
@Order(50)
public class AudiencePolicy implements JwtValidationPolicy {

    @Override
    public void validate(JwtValidationContext context) {
        String expectedAudience = context.properties().getAudience();
        if (expectedAudience == null || expectedAudience.isBlank()) {
            return;
        }

        List<String> audience = context.claims().getAudience();
        if (audience == null || !audience.contains(expectedAudience)) {
            throw new JwtAuthenticationException(MSG_INVALID_AUDIENCE);
        }
    }
}

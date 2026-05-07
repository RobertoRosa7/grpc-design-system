package br.com.orderflow.document.infra.adapter.security.validation;

import br.com.orderflow.document.infra.adapter.security.policy.JwtValidationPolicy;
import br.com.orderflow.document.infra.config.security.JwtSecurityProperties;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Orquestra as politicas de validacao de claims JWT.
 */
@Component
public class JwtClaimsValidator {

    private final List<JwtValidationPolicy> policies;

    public JwtClaimsValidator(List<JwtValidationPolicy> policies) {
        this.policies = policies;
    }

    public void validate(JWTClaimsSet claims, JwtSecurityProperties properties) {
        Instant now = Instant.now();
        long skewSeconds = Math.max(0, properties.getClockSkewSeconds());
        JwtValidationContext context = new JwtValidationContext(claims, properties, now, skewSeconds);

        for (JwtValidationPolicy policy : policies) {
            policy.validate(context);
        }
    }
}

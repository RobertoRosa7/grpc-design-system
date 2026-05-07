package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_TOKEN_EXPIRED_OR_MISSING_EXP;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.validation.JwtValidationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Valida a claim de expiracao (exp).
 */
@Component
@Order(10)
public class ExpirationPolicy implements JwtValidationPolicy {

    @Override
    public void validate(JwtValidationContext context) {
        Date expiration = context.claims().getExpirationTime();
        if (expiration == null || context.now().minusSeconds(context.skewSeconds()).isAfter(expiration.toInstant())) {
            throw new JwtAuthenticationException(MSG_TOKEN_EXPIRED_OR_MISSING_EXP);
        }
    }
}

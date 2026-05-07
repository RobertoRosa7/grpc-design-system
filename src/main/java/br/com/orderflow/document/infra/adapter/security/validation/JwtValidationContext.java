package br.com.orderflow.document.infra.adapter.security.validation;

import br.com.orderflow.document.infra.config.security.JwtSecurityProperties;
import com.nimbusds.jwt.JWTClaimsSet;

import java.time.Instant;

/**
 * Contexto compartilhado pelas politicas de validacao JWT.
 */
public record JwtValidationContext(
    JWTClaimsSet claims,
    JwtSecurityProperties properties,
    Instant now,
    long skewSeconds
) {
}

package br.com.orderflow.document.infra.adapter.security;

import java.util.List;
import java.util.Map;

/**
 * Representa o principal autenticado por JWT.
 */
public record JwtPrincipal(
    String subject,
    List<String> roles,
    Map<String, Object> claims
) {
}

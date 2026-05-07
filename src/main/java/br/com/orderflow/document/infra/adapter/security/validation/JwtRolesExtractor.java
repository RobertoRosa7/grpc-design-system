package br.com.orderflow.document.infra.adapter.security.validation;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Extrai roles da claim de forma tolerante a formatos comuns.
 */
@Component
public class JwtRolesExtractor {

    public List<String> extract(Object rolesClaim) {
        if (rolesClaim instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        if (rolesClaim instanceof String role) {
            return List.of(role);
        }
        return List.of();
    }
}

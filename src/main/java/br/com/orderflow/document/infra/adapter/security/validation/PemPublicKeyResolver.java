package br.com.orderflow.document.infra.adapter.security.validation;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_INVALID_PEM;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_PEM_CONVERSION_FAILED;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.policy.PemPublicKeyPolicy;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.List;

/**
 * Orquestra policies de parse de chave publica PEM.
 */
@Component
public class PemPublicKeyResolver {

    private final List<PemPublicKeyPolicy> policies;

    public PemPublicKeyResolver(List<PemPublicKeyPolicy> policies) {
        this.policies = policies;
    }

    public PublicKey resolve(String pem) {
        for (PemPublicKeyPolicy policy : policies) {
            if (policy.supports(pem)) {
                try {
                    return policy.parse(pem);
                } catch (JwtAuthenticationException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new JwtAuthenticationException(MSG_PEM_CONVERSION_FAILED, ex);
                }
            }
        }

        throw new JwtAuthenticationException(MSG_INVALID_PEM);
    }
}

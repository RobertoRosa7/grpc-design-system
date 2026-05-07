package br.com.orderflow.document.infra.adapter.security.validation;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_SIGNATURE_INVALID;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_SIGNATURE_VALIDATION_FAILED;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

/**
 * Valida assinatura do token JWT.
 */
@Component
public class JwtSignatureValidator {

    private final DefaultJWSVerifierFactory verifierFactory;

    public JwtSignatureValidator() {
        this.verifierFactory = new DefaultJWSVerifierFactory();
    }

    public void validate(SignedJWT signedJwt, PublicKey publicKey) {
        try {
            JWSVerifier verifier = verifierFactory.createJWSVerifier(signedJwt.getHeader(), publicKey);
            if (!signedJwt.verify(verifier)) {
                throw new JwtAuthenticationException(MSG_SIGNATURE_INVALID);
            }
        } catch (JwtAuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JwtAuthenticationException(MSG_SIGNATURE_VALIDATION_FAILED, ex);
        }
    }
}

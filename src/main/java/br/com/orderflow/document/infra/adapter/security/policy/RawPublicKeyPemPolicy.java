package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.ALGORITHM_EC;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.ALGORITHM_RSA;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.EMPTY;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.PUBLIC_KEY_BEGIN;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.PUBLIC_KEY_END;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.PUBLIC_KEY_MARKER;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.WHITESPACE_REGEX;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Policy para extrair chave publica de PEM com bloco PUBLIC KEY.
 */
@Component
@Order(20)
public class RawPublicKeyPemPolicy implements PemPublicKeyPolicy {

    @Override
    public boolean supports(String pem) {
        return pem != null && pem.contains(PUBLIC_KEY_MARKER);
    }

    @Override
    public PublicKey parse(String pem) throws Exception {
        String sanitized = pem
                .replace(PUBLIC_KEY_BEGIN, EMPTY)
                .replace(PUBLIC_KEY_END, EMPTY)
                .replaceAll(WHITESPACE_REGEX, EMPTY);

        byte[] der = Base64.getDecoder().decode(sanitized);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        try {
            return KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(spec);
        } catch (Exception rsaEx) {
            return KeyFactory.getInstance(ALGORITHM_EC).generatePublic(spec);
        }
    }
}

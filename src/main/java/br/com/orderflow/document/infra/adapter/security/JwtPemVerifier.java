package br.com.orderflow.document.infra.adapter.security;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.CLAIM_ROLES;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_JWT_VALIDATION_FAILED;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_PEM_LOAD_FAILED;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.MSG_PEM_NOT_FOUND_PREFIX;

import br.com.orderflow.document.infra.adapter.security.exception.JwtAuthenticationException;
import br.com.orderflow.document.infra.adapter.security.validation.JwtClaimsValidator;
import br.com.orderflow.document.infra.adapter.security.validation.JwtRolesExtractor;
import br.com.orderflow.document.infra.adapter.security.validation.JwtSignatureValidator;
import br.com.orderflow.document.infra.adapter.security.validation.PemPublicKeyResolver;
import br.com.orderflow.document.infra.config.security.JwtSecurityProperties;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.List;

/**
 * Valida assinatura e claims de JWT usando chave publica em PEM.
 */
@Component
public class JwtPemVerifier {

    private final JwtSecurityProperties properties;
    private final ResourceLoader resourceLoader;
    private final JwtClaimsValidator jwtClaimsValidator;
    private final JwtSignatureValidator jwtSignatureValidator;
    private final JwtRolesExtractor jwtRolesExtractor;
    private final PemPublicKeyResolver pemPublicKeyResolver;
    private volatile PublicKey cachedPublicKey;

    public JwtPemVerifier(
            JwtSecurityProperties properties,
            ResourceLoader resourceLoader,
            JwtClaimsValidator jwtClaimsValidator,
            JwtSignatureValidator jwtSignatureValidator,
            JwtRolesExtractor jwtRolesExtractor,
            PemPublicKeyResolver pemPublicKeyResolver) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
        this.jwtClaimsValidator = jwtClaimsValidator;
        this.jwtSignatureValidator = jwtSignatureValidator;
        this.jwtRolesExtractor = jwtRolesExtractor;
        this.pemPublicKeyResolver = pemPublicKeyResolver;
    }

    public JwtPrincipal validate(String token) {
        try {
            SignedJWT signedJwt = SignedJWT.parse(token);
            jwtSignatureValidator.validate(signedJwt, getPublicKey());

            JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
            jwtClaimsValidator.validate(claims, properties);

            String subject = claims.getSubject();
            List<String> roles = jwtRolesExtractor.extract(claims.getClaim(CLAIM_ROLES));
            return new JwtPrincipal(subject, roles, claims.getClaims());
        } catch (JwtAuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JwtAuthenticationException(MSG_JWT_VALIDATION_FAILED, ex);
        }
    }

    private PublicKey getPublicKey() {
        PublicKey key = cachedPublicKey;
        if (key != null) {
            return key;
        }
        synchronized (this) {
            if (cachedPublicKey == null) {
                cachedPublicKey = loadPublicKey();
            }
            return cachedPublicKey;
        }
    }

    private PublicKey loadPublicKey() {
        try {
            Resource resource = resourceLoader.getResource(properties.getCertificatePath());
            if (!resource.exists()) {
                throw new JwtAuthenticationException(MSG_PEM_NOT_FOUND_PREFIX + properties.getCertificatePath());
            }

            String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return pemPublicKeyResolver.resolve(pem);
        } catch (JwtAuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JwtAuthenticationException(MSG_PEM_LOAD_FAILED, ex);
        }
    }
}

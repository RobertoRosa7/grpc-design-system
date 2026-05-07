package br.com.orderflow.document.infra.adapter.security.policy;

import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.CERTIFICATE_MARKER;
import static br.com.orderflow.document.infra.adapter.security.constant.JwtSecurityLiterals.X509_TYPE;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Policy para extrair chave publica de PEM com bloco CERTIFICATE.
 */
@Component
@Order(10)
public class CertificatePemPublicKeyPolicy implements PemPublicKeyPolicy {

    @Override
    public boolean supports(String pem) {
        return pem != null && pem.contains(CERTIFICATE_MARKER);
    }

    @Override
    public PublicKey parse(String pem) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509_TYPE);
        try (ByteArrayInputStream input = new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8))) {
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(input);
            return certificate.getPublicKey();
        }
    }
}

package br.com.orderflow.document.infra.adapter.security.policy;

import java.security.PublicKey;

/**
 * Contrato para parse de chave publica a partir de PEM.
 */
public interface PemPublicKeyPolicy {

    boolean supports(String pem);

    PublicKey parse(String pem) throws Exception;
}

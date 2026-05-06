package br.com.orderflow.document.domain.policy;

import br.com.orderflow.document.domain.model.DocumentPayload;

import java.util.stream.Stream;

/**
 * Contrato de policy de domínio para payload de documento.
 */
public interface DocumentPayloadPolicy {

  Stream<String> validate(DocumentPayload payload);
}

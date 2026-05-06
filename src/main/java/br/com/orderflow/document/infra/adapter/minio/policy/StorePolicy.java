package br.com.orderflow.document.infra.adapter.minio.policy;

import br.com.orderflow.document.infra.adapter.minio.validation.StoreContext;

import java.util.stream.Stream;

/**
 * Contrato de policy para operação de storage.
 */
public interface StorePolicy {

    Stream<String> validate(StoreContext context);
}

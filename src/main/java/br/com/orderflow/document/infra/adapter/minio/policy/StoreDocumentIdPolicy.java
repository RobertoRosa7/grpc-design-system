package br.com.orderflow.document.infra.adapter.minio.policy;

import br.com.orderflow.document.infra.adapter.minio.constant.MinioConstants;
import br.com.orderflow.document.infra.adapter.minio.validation.StoreContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Policy para validar documentId na operação de storage.
 */
@Component
@Order(10)
public class StoreDocumentIdPolicy implements StorePolicy {

  @Override
  public Stream<String> validate(StoreContext context) {
    return hasText(context.documentId())
        ? Stream.empty()
      : Stream.of(MinioConstants.POLICY_STORE_DOCUMENT_ID_REQUIRED);
  }

  private boolean hasText(String value) {
    return value != null && !value.isBlank();
  }
}

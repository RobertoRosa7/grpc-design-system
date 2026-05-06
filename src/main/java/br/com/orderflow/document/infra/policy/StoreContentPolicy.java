package br.com.orderflow.document.infra.policy;

import br.com.orderflow.document.infra.constant.InfraConstants;
import br.com.orderflow.document.infra.validation.StoreContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Policy para validar conteúdo do arquivo na operação de storage.
 */
@Component
@Order(20)
public class StoreContentPolicy implements StorePolicy {

  @Override
  public Stream<String> validate(StoreContext context) {
    boolean hasContent = context.content() != null && context.content().length > 0;
    return hasContent
        ? Stream.empty()
        : Stream.of(InfraConstants.POLICY_STORE_CONTENT_REQUIRED);
  }
}

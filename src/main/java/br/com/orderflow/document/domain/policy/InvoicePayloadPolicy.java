package br.com.orderflow.document.domain.policy;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.model.DocumentType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Policy de validação para payload INVOICE.
 */
@Component
@Order(30)
public class InvoicePayloadPolicy implements DocumentPayloadPolicy {

  @Override
  public Stream<String> validate(DocumentPayload payload) {
    return Optional.ofNullable(payload.getType())
        .filter(type -> type == DocumentType.INVOICE)
        .map(type -> Stream.of(
            hasText(payload.getCustomerId()) ? null : DomainConstants.POLICY_INVOICE_CUSTOMER_REQUIRED,
            hasText(payload.getOrderId()) ? null : DomainConstants.POLICY_INVOICE_ORDER_REQUIRED,
            payload.getItems() != null && !payload.getItems().isEmpty() ? null : DomainConstants.POLICY_INVOICE_ITEMS_REQUIRED
        ).filter(Objects::nonNull))
        .orElseGet(Stream::empty);
  }

  private boolean hasText(String value) {
    return value != null && !value.isBlank();
  }
}

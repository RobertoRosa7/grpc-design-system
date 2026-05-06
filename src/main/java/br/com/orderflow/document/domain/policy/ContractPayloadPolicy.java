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
 * Policy de validação para payload CONTRACT.
 */
@Component
@Order(40)
public class ContractPayloadPolicy implements DocumentPayloadPolicy {

  @Override
  public Stream<String> validate(DocumentPayload payload) {
    return Optional.ofNullable(payload.getType())
        .filter(type -> type == DocumentType.CONTRACT)
        .map(type -> Stream.of(
            hasText(payload.getPartyA()) ? null : DomainConstants.POLICY_CONTRACT_PARTY_A_REQUIRED,
            hasText(payload.getPartyB()) ? null : DomainConstants.POLICY_CONTRACT_PARTY_B_REQUIRED,
            hasText(payload.getContractContent()) ? null : DomainConstants.POLICY_CONTRACT_CONTENT_REQUIRED
        ).filter(Objects::nonNull))
        .orElseGet(Stream::empty);
  }

  private boolean hasText(String value) {
    return value != null && !value.isBlank();
  }
}

package br.com.orderflow.document.domain.policy;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.model.DocumentPayload;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Policy que exige tipo do documento informado.
 */
@Component
@Order(20)
public class DocumentTypeRequiredPolicy implements DocumentPayloadPolicy {

    @Override
    public Stream<String> validate(DocumentPayload payload) {
        return payload.getType() != null
                ? Stream.empty()
                : Stream.of(DomainConstants.POLICY_TYPE_REQUIRED);
    }
}

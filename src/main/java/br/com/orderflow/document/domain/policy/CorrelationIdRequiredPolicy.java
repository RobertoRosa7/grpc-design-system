package br.com.orderflow.document.domain.policy;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.model.DocumentPayload;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Policy que exige correlationId informado.
 */
@Component
@Order(10)
public class CorrelationIdRequiredPolicy implements DocumentPayloadPolicy {

    @Override
    public Stream<String> validate(DocumentPayload payload) {
        return hasText(payload.getCorrelationId())
                ? Stream.empty()
                : Stream.of(DomainConstants.POLICY_CORRELATION_REQUIRED);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

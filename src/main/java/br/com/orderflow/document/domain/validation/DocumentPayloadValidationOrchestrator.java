package br.com.orderflow.document.domain.validation;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.exception.DomainValidationException;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.policy.DocumentPayloadPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Orquestrador de validação de domínio baseado em policies dinâmicas.
 */
@Component
public class DocumentPayloadValidationOrchestrator {

    private final List<DocumentPayloadPolicy> policies;

    public DocumentPayloadValidationOrchestrator(List<DocumentPayloadPolicy> policies) {
        this.policies = policies;
    }

    public void validate(DocumentPayload payload) {
        List<String> violations = policies.stream()
                .flatMap(policy -> policy.validate(payload))
                .distinct()
                .toList();

        String message = violations.isEmpty()
                ? DomainConstants.EMPTY_VALUE
                : DomainConstants.ERROR_PREFIX_VALIDATION + violations.stream().collect(Collectors.joining(", "));

        Optional.of(message)
                .filter(msg -> !msg.isEmpty())
                .ifPresent(msg -> {
                    throw new DomainValidationException(msg);
                });
    }
}

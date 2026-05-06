package br.com.orderflow.document.infra.validation;

import br.com.orderflow.document.infra.exception.InfraStorageException;
import br.com.orderflow.document.infra.policy.StorePolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Orquestrador de validação da operação de storage.
 */
@Component
public class StoreValidationOrchestrator {

  private final List<StorePolicy> policies;

  public StoreValidationOrchestrator(List<StorePolicy> policies) {
    this.policies = policies;
  }

  public void validate(StoreContext context) {
    List<String> violations = policies.stream()
        .flatMap(policy -> policy.validate(context))
        .distinct()
        .toList();

    String message = violations.stream().collect(Collectors.joining(", "));
    Optional.of(message)
        .filter(msg -> !msg.isBlank())
        .ifPresent(msg -> {
          throw new InfraStorageException(msg);
        });
  }
}

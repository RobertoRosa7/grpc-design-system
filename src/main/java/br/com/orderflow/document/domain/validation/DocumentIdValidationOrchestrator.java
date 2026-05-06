package br.com.orderflow.document.domain.validation;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.exception.DomainValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Validação de identificador do documento.
 */
@Component
public class DocumentIdValidationOrchestrator {

  public void validate(String documentId) {
    boolean isInvalid = documentId == null || documentId.isBlank();
    Optional.of(isInvalid)
        .filter(Boolean::booleanValue)
        .ifPresent(value -> {
          throw new DomainValidationException(DomainConstants.ERROR_PREFIX_VALIDATION + "documentId é obrigatório");
        });
  }
}

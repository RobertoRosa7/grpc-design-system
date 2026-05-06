package br.com.orderflow.document.domain.exception;

/**
 * Exceção para violações de policy e validação de domínio.
 */
public class DomainValidationException extends DomainException {

    public DomainValidationException(String message) {
        super(message);
    }
}

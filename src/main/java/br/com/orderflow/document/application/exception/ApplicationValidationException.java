package br.com.orderflow.document.application.exception;

/**
 * Exceção de validação na camada de aplicação.
 */
public class ApplicationValidationException extends ApplicationException {

    public ApplicationValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

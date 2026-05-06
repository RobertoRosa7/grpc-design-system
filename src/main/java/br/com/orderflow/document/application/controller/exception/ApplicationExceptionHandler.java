package br.com.orderflow.document.application.controller.exception;

import br.com.orderflow.document.application.controller.dto.ErrorResponse;
import br.com.orderflow.document.application.exception.ApplicationException;
import br.com.orderflow.document.application.exception.ApplicationNotFoundException;
import br.com.orderflow.document.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Handler de exceções da API REST.
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(ex.getMessage(), "N/A", Instant.now()));
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
    HttpStatus status = ex instanceof ApplicationNotFoundException
        ? HttpStatus.NOT_FOUND
        : HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(status)
        .body(new ErrorResponse(ex.getMessage(), "N/A", Instant.now()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(ex.getMessage(), "N/A", Instant.now()));
  }
}

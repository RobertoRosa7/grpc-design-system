package br.com.orderflow.document.application.web.controller.dto;

import java.time.Instant;

/**
 * Payload padrão de erro da API REST.
 */
public record ErrorResponse(
    String message,
    String path,
    Instant timestamp
) {
}

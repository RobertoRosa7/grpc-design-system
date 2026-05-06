package br.com.orderflow.document.application.web.constant;

/**
 * Constantes da camada de aplicação.
 */
public final class WebConstants {

  private WebConstants() {
  }

  public static final String GRPC_ERROR_INTERNAL = "Erro interno ao processar documento";
  public static final String GRPC_ERROR_NOT_FOUND_PREFIX = "Documento não encontrado: ";
  public static final String GRPC_ERROR_INVALID_ARGUMENT = "Requisição inválida";

  public static final String REST_BASE_PATH = "/api/v1/documents";
  public static final String REST_PATH_ID = "/{documentId}";

  public static final String HEADER_CORRELATION_ID = "X-Correlation-Id";
}

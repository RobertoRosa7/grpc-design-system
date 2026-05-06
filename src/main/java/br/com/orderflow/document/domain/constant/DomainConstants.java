package br.com.orderflow.document.domain.constant;

/**
 * Constantes da camada de domínio.
 */
public final class DomainConstants {

  private DomainConstants() {
  }

  public static final String EMPTY_VALUE = "";
  public static final String ERROR_PREFIX_VALIDATION = "Validação de domínio falhou: ";
  public static final String ERROR_DOCUMENT_NOT_FOUND = "Documento não encontrado: ";
  public static final String ERROR_GENERATE_DOCUMENT = "Falha ao gerar documento";

  public static final String POLICY_CORRELATION_REQUIRED = "correlationId é obrigatório";
  public static final String POLICY_TYPE_REQUIRED = "type é obrigatório";
  public static final String POLICY_INVOICE_CUSTOMER_REQUIRED = "customerId é obrigatório para INVOICE";
  public static final String POLICY_INVOICE_ORDER_REQUIRED = "orderId é obrigatório para INVOICE";
  public static final String POLICY_INVOICE_ITEMS_REQUIRED = "items é obrigatório para INVOICE";
  public static final String POLICY_CONTRACT_PARTY_A_REQUIRED = "partyA é obrigatório para CONTRACT";
  public static final String POLICY_CONTRACT_PARTY_B_REQUIRED = "partyB é obrigatório para CONTRACT";
  public static final String POLICY_CONTRACT_CONTENT_REQUIRED = "contractContent é obrigatório para CONTRACT";
  public static final String POLICY_REPORT_TITLE_REQUIRED = "reportTitle é obrigatório para REPORT";
  public static final String POLICY_REPORT_CONTENT_REQUIRED = "reportContent é obrigatório para REPORT";
}

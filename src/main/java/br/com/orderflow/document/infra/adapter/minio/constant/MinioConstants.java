package br.com.orderflow.document.infra.adapter.minio.constant;

/**
 * Constantes da camada de infraestrutura.
 */
public final class MinioConstants {

  private MinioConstants() {
  }

  public static final String CONTENT_TYPE_PDF = "application/pdf";
  public static final String PDF_EXTENSION = ".pdf";
  public static final String STORAGE_PATH_SEPARATOR = "/";

  public static final String ERROR_STORAGE_PREFIX = "Falha ao armazenar PDF no MinIO [documentId=";
  public static final String ERROR_STORAGE_SUFFIX = "]";
  public static final String ERROR_PDF_RENDER = "Falha ao renderizar PDF";
  public static final String ERROR_PERSISTENCE = "Falha de persistência no MongoDB";

  public static final String POLICY_STORE_DOCUMENT_ID_REQUIRED = "documentId é obrigatório";
  public static final String POLICY_STORE_CONTENT_REQUIRED = "content é obrigatório";
  public static final String POLICY_STORE_BUCKET_REQUIRED = "bucket é obrigatório";
}

package br.com.orderflow.document.infra.adapter.pdf.exception;

/**
 * Exceção para falhas de renderização de PDF.
 */
public class PdfRenderException extends RuntimeException {

  public PdfRenderException(String message, Throwable cause) {
    super(message, cause);
  }
}

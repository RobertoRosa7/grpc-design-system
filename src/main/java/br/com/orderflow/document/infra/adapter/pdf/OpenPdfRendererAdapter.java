package br.com.orderflow.document.infra.adapter.pdf;

import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.model.DocumentType;
import br.com.orderflow.document.domain.port.out.PdfRendererPort;
import br.com.orderflow.document.infra.constant.InfraConstants;
import br.com.orderflow.document.infra.exception.InfraRenderException;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

/**
 * Adaptador de saída: renderiza PDF usando a biblioteca OpenPDF.
 * Implementa PdfRendererPort.
 * Se a equipe decidir migrar para iText
 * ou Apache FOP, apenas este componente precisa ser substituído.
 * O caso de uso permanece inalterado.
 */
@Component
public class OpenPdfRendererAdapter implements PdfRendererPort {

  private static final Logger log = LoggerFactory.getLogger(OpenPdfRendererAdapter.class);

  @Override
  public byte[] render(DocumentPayload payload) {
    log.debug("Renderizando PDF [type={}] [correlationId={}]",
        payload.getType(), payload.getCorrelationId());

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      Document pdfDocument = new Document(PageSize.A4);
      PdfWriter.getInstance(pdfDocument, outputStream);
      pdfDocument.open();

      addHeader(pdfDocument, payload);
      addBody(pdfDocument, payload);
      addFooter(pdfDocument, payload);

      pdfDocument.close();
      return outputStream.toByteArray();

    } catch (Exception e) {
      throw new InfraRenderException(InfraConstants.ERROR_PDF_RENDER, e);
    }
  }

  private void addHeader(Document doc, DocumentPayload payload) throws Exception {
    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
    String title = resolveTitle(payload);
    doc.add(new Paragraph(title, titleFont));
    doc.add(new Paragraph(" "));
  }

  private void addBody(Document doc, DocumentPayload payload) throws Exception {
    Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);

    switch (payload.getType()) {
      case INVOICE -> addInvoiceBody(doc, payload, bodyFont);
      case CONTRACT -> addContractBody(doc, payload, bodyFont);
      case REPORT -> addReportBody(doc, payload, bodyFont);
    }
  }

  private void addInvoiceBody(Document doc, DocumentPayload payload, Font bodyFont) throws Exception {
    doc.add(new Paragraph("Cliente: " + payload.getCustomerId(), bodyFont));
    doc.add(new Paragraph("Pedido: " + payload.getOrderId(), bodyFont));
    doc.add(new Paragraph(" "));

    for (DocumentPayload.LineItem item : payload.getItems() == null ? java.util.List.<DocumentPayload.LineItem>of() : payload.getItems()) {
      String line = String.format("  %s  x%d  @ %.2f", item.description(), item.quantity(), item.unitPrice());
      doc.add(new Paragraph(line, bodyFont));
    }

    doc.add(new Paragraph(" "));
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    doc.add(new Paragraph(String.format("Total: %.2f %s", payload.getTotalAmount(), payload.getCurrency()), boldFont));
  }

  private void addContractBody(Document doc, DocumentPayload payload, Font bodyFont) throws Exception {
    doc.add(new Paragraph("Parte A: " + payload.getPartyA(), bodyFont));
    doc.add(new Paragraph("Parte B: " + payload.getPartyB(), bodyFont));
    doc.add(new Paragraph("Válido até: " + payload.getValidUntil(), bodyFont));
    doc.add(new Paragraph(" "));
    doc.add(new Paragraph(payload.getContractContent(), bodyFont));
  }

  private void addReportBody(Document doc, DocumentPayload payload, Font bodyFont) throws Exception {
    doc.add(new Paragraph("Período: " + payload.getPeriod(), bodyFont));
    doc.add(new Paragraph(" "));
    doc.add(new Paragraph(payload.getReportContent(), bodyFont));
  }

  private void addFooter(Document doc, DocumentPayload payload) throws Exception {
    Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);
    doc.add(new Paragraph(" "));
    doc.add(new Paragraph("Gerado por OrderFlow Document Service", footerFont));
    doc.add(new Paragraph("Correlation ID: " + payload.getCorrelationId(), footerFont));
  }

  private String resolveTitle(DocumentPayload payload) {
    return switch (payload.getType()) {
      case INVOICE -> "Nota Fiscal";
      case CONTRACT -> "Contrato";
      case REPORT -> payload.getReportTitle() != null ? payload.getReportTitle() : "Relatório";
    };
  }
}

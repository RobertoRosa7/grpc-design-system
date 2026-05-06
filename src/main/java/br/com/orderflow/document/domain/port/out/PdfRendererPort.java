package br.com.orderflow.document.domain.port.out;

import br.com.orderflow.document.domain.model.DocumentPayload;

/**
 * Porta de saída: abstrai a tecnologia de renderização de PDF.
 * <p>
 * A implementação atual usa OpenPDF ({@code OpenPdfRendererAdapter}).
 * Pode ser substituída por iText, Apache FOP ou qualquer outra biblioteca
 * sem alterar o caso de uso.
 */
public interface PdfRendererPort {

    /**
     * Renderiza um documento PDF a partir do payload fornecido.
     *
     * @param payload dados estruturados do documento
     * @return bytes do PDF gerado
     */
    byte[] render(DocumentPayload payload);
}

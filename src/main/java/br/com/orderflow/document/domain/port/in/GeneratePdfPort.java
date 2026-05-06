package br.com.orderflow.document.domain.port.in;

import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;

/**
 * Porta de entrada: define o contrato para geração de um documento PDF.
 * <p>
 * Qualquer adaptador de entrada (gRPC, REST, CLI) invoca esta porta
 * sem conhecer detalhes de renderização ou persistência.
 */
public interface GeneratePdfPort {

    /**
     * Executa a geração de um documento PDF a partir do payload fornecido.
     *
     * @param payload dados estruturados necessários para renderizar o PDF
     * @return entidade {@link Document} com id, status e caminho de armazenamento
     */
    Document execute(DocumentPayload payload);
}

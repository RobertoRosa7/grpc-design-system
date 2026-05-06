package br.com.orderflow.document.domain.port.in;

import br.com.orderflow.document.domain.model.Document;

import java.util.Optional;

/**
 * Porta de entrada: recupera um documento previamente gerado pelo seu id.
 */
public interface GetDocumentPort {

    /**
     * @param documentId identificador único do documento
     * @return {@link Optional} com o documento, ou vazio se não encontrado
     */
    Optional<Document> execute(String documentId);
}

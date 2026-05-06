package br.com.orderflow.document.domain.port.out;

import br.com.orderflow.document.domain.model.Document;

import java.util.Optional;

/**
 * Porta de saída: abstrai o mecanismo de persistência de documentos.
 * <p>
 * A implementação atual usa MongoDB ({@code DocumentMongoRepositoryAdapter}).
 * A troca do banco não afeta o caso de uso.
 */
public interface DocumentRepositoryPort {

    /**
     * Persiste o documento e retorna a instância com id atribuído.
     *
     * @param document entidade de domínio a ser salva
     * @return documento com id preenchido pela camada de persistência
     */
    Document save(Document document);

    /**
     * Recupera um documento pelo seu id único.
     *
     * @param id identificador do documento
     * @return {@link Optional} com o documento ou vazio se não existir
     */
    Optional<Document> findById(String id);
}

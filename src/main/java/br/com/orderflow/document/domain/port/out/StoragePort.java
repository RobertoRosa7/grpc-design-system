package br.com.orderflow.document.domain.port.out;

/**
 * Porta de saída: abstrai o armazenamento do binário do documento.
 * <p>
 * A implementação atual usa MinIO ({@code MinioStorageAdapter}).
 * A troca do provedor de armazenamento não afeta o domínio.
 */
public interface StoragePort {

    /**
     * Armazena o conteúdo binário do documento e retorna o caminho de acesso.
     *
     * @param documentId identificador único do documento (usado como object key)
     * @param content    bytes do PDF gerado
     * @return caminho/URL de acesso ao objeto armazenado
     */
    String store(String documentId, byte[] content);
}

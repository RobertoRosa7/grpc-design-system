package br.com.orderflow.document.domain.service;

import br.com.orderflow.document.domain.model.Document;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.port.in.GeneratePdfPort;
import br.com.orderflow.document.domain.port.in.GetDocumentPort;
import br.com.orderflow.document.domain.port.out.DocumentRepositoryPort;
import br.com.orderflow.document.domain.port.out.PdfRendererPort;
import br.com.orderflow.document.domain.port.out.StoragePort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Caso de uso central do serviço.
 * <p>
 * Orquestra a renderização PDF e a persistência sem depender
 * de tecnologia de transporte ou implementação de infraestrutura.
 * Toda dependência é injetada via interfaces (portas).
 */
@Service
public class GeneratePdfService implements GeneratePdfPort, GetDocumentPort {

    private static final Logger log = LoggerFactory.getLogger(GeneratePdfService.class);

    private final PdfRendererPort pdfRenderer;
    private final StoragePort storagePort;
    private final DocumentRepositoryPort documentRepository;

    public GeneratePdfService(
            PdfRendererPort pdfRenderer,
            StoragePort storagePort,
            DocumentRepositoryPort documentRepository) {
        this.pdfRenderer = pdfRenderer;
        this.storagePort = storagePort;
        this.documentRepository = documentRepository;
    }

    @Override
    public Document execute(DocumentPayload payload) {
        log.info("Iniciando geração de documento [correlationId={}] [type={}]",
                payload.getCorrelationId(), payload.getType());

        // 1. Cria entidade em estado PROCESSING
        Document document = Document.create(payload.getCorrelationId(), payload.getType());
        Document saved = documentRepository.save(document);

        try {
            // 2. Renderiza o PDF via porta de saída
            byte[] pdfBytes = pdfRenderer.render(payload);

            // 3. Armazena o binário no MinIO e obtém o caminho de referência
            String storagePath = storagePort.store(saved.getId(), pdfBytes);

            // 4. Atualiza entidade como COMPLETED com apenas a referência
            saved.markCompleted(storagePath);
            Document completed = documentRepository.save(saved);

            log.info("Documento gerado com sucesso [id={}] [storagePath={}]",
                    completed.getId(), completed.getStoragePath());

            return completed;

        } catch (Exception e) {
            log.error("Falha na geração do documento [id={}]", saved.getId(), e);
            saved.markFailed();
            documentRepository.save(saved);
            throw new RuntimeException("Falha ao gerar documento: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Document> execute(String documentId) {
        log.debug("Consultando documento [id={}]", documentId);
        return documentRepository.findById(documentId);
    }
}

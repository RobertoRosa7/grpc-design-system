package br.com.orderflow.document.infra.adapter.minio;

import br.com.orderflow.document.domain.port.out.StoragePort;
import br.com.orderflow.document.infra.adapter.minio.constant.MinioConstants;
import br.com.orderflow.document.infra.adapter.minio.exception.MinioStorageException;
import br.com.orderflow.document.infra.adapter.minio.policy.BucketProvisionPolicy;
import br.com.orderflow.document.infra.adapter.minio.validation.StoreContext;
import br.com.orderflow.document.infra.adapter.minio.validation.StoreValidationOrchestrator;
import br.com.orderflow.document.infra.config.mongodb.MinioProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * Adaptador de saída: armazena binários de PDF no MinIO.
 * Implementa StoragePort.
 * O domínio recebe apenas o caminho
 * de referência bucket/documentId.pdf e nunca manipula
 * o binário após a geração.
 *
 * <p>Resiliência aplicada:
 * <ul>
 *   <li>{@code @Retry} — até 3 tentativas com backoff exponencial para falhas transitórias.</li>
 *   <li>{@code @CircuitBreaker} — abre o circuito após 50% de falhas em janela de 10 chamadas.</li>
 * </ul>
 */
@Component
public class MinioStorageAdapter implements StoragePort {

    private static final Logger log = LoggerFactory.getLogger(MinioStorageAdapter.class);
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final StoreValidationOrchestrator storeValidation;
    private final BucketProvisionPolicy bucketProvisionPolicy;

    public MinioStorageAdapter(
            MinioClient minioClient,
            MinioProperties minioProperties,
            StoreValidationOrchestrator storeValidation,
            BucketProvisionPolicy bucketProvisionPolicy) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.storeValidation = storeValidation;
        this.bucketProvisionPolicy = bucketProvisionPolicy;
    }

    @Override
    @Retry(name = "minio", fallbackMethod = "storeFallback")
    @CircuitBreaker(name = "minio", fallbackMethod = "storeFallback")
    public String store(String documentId, byte[] content) {
        String bucket = minioProperties.getBucket();
        String objectKey = documentId + MinioConstants.PDF_EXTENSION;
        storeValidation.validate(new StoreContext(documentId, content, bucket));

        try {
            bucketProvisionPolicy.apply(minioClient, bucket);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(new ByteArrayInputStream(content), content.length, -1)
                            .contentType(MinioConstants.CONTENT_TYPE_PDF)
                            .build());

            String storagePath = bucket + MinioConstants.STORAGE_PATH_SEPARATOR + objectKey;
            log.info("PDF armazenado no MinIO [path={}]", storagePath);
            return storagePath;

        } catch (Exception e) {
            throw new MinioStorageException(
                    MinioConstants.ERROR_STORAGE_PREFIX + documentId + MinioConstants.ERROR_STORAGE_SUFFIX,
                    e);
        }
    }

    /**
     * Fallback acionado quando retries se esgotam ou o circuito está aberto.
     * Lança a exceção original encapsulada para que o caso de uso trate adequadamente.
     */
    @SuppressWarnings("unused")
    private String storeFallback(String documentId, byte[] content, Exception ex) {
        log.error("Fallback acionado para MinIO [documentId={}]: {}", documentId, ex.getMessage());
        throw new MinioStorageException(
                MinioConstants.ERROR_STORAGE_PREFIX + documentId + MinioConstants.ERROR_STORAGE_SUFFIX,
                ex);
    }
}

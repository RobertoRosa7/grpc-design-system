package br.com.orderflow.document.infra.adapter.minio;

import br.com.orderflow.document.domain.port.out.StoragePort;
import br.com.orderflow.document.infra.adapter.minio.constant.MinioConstants;
import br.com.orderflow.document.infra.adapter.minio.exception.MinioStorageException;
import br.com.orderflow.document.infra.adapter.minio.policy.BucketProvisionPolicy;
import br.com.orderflow.document.infra.adapter.minio.validation.StoreContext;
import br.com.orderflow.document.infra.adapter.minio.validation.StoreValidationOrchestrator;
import br.com.orderflow.document.infra.config.mongodb.MinioProperties;
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
}

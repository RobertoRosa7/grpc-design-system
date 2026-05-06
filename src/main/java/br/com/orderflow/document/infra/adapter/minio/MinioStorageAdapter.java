package br.com.orderflow.document.infra.adapter.minio;

import br.com.orderflow.document.domain.port.out.StoragePort;
import br.com.orderflow.document.infra.config.MinioProperties;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * Adaptador de saída: armazena binários de PDF no MinIO.
 * <p>
 * Implementa {@link StoragePort}. O domínio recebe apenas o caminho
 * de referência ({@code bucket/documentId.pdf}) — nunca manipula
 * o binário após a geração.
 */
@Component
public class MinioStorageAdapter implements StoragePort {

    private static final Logger log = LoggerFactory.getLogger(MinioStorageAdapter.class);
    private static final String CONTENT_TYPE = "application/pdf";

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioStorageAdapter(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Override
    public String store(String documentId, byte[] content) {
        String bucket = minioProperties.getBucket();
        String objectKey = documentId + ".pdf";

        try {
            ensureBucketExists(bucket);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(new ByteArrayInputStream(content), content.length, -1)
                            .contentType(CONTENT_TYPE)
                            .build());

            String storagePath = bucket + "/" + objectKey;
            log.info("PDF armazenado no MinIO [path={}]", storagePath);
            return storagePath;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao armazenar PDF no MinIO [documentId=" + documentId + "]", e);
        }
    }

    private void ensureBucketExists(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("Bucket criado no MinIO [bucket={}]", bucket);
        }
    }
}

package br.com.orderflow.document.infra.policy;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Policy de provisionamento de bucket no MinIO.
 */
@Component
public class BucketProvisionPolicy {

  private static final Logger log = LoggerFactory.getLogger(BucketProvisionPolicy.class);

  public void apply(MinioClient minioClient, String bucket) throws Exception {
    boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    Runnable action = exists
        ? () -> { }
        : () -> {
          try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("Bucket criado no MinIO [bucket={}]", bucket);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        };
    action.run();
  }
}

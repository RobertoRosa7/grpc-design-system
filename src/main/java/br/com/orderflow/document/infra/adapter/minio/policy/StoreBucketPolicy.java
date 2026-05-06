package br.com.orderflow.document.infra.adapter.minio.policy;

import br.com.orderflow.document.infra.adapter.minio.constant.MinioConstants;
import br.com.orderflow.document.infra.adapter.minio.validation.StoreContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Policy para validar bucket na operação de storage.
 */
@Component
@Order(30)
public class StoreBucketPolicy implements StorePolicy {

    @Override
    public Stream<String> validate(StoreContext context) {
        return hasText(context.bucket())
                ? Stream.empty()
            : Stream.of(MinioConstants.POLICY_STORE_BUCKET_REQUIRED);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

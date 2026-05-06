package br.com.orderflow.document.infra.policy;

import br.com.orderflow.document.infra.constant.InfraConstants;
import br.com.orderflow.document.infra.validation.StoreContext;
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
                : Stream.of(InfraConstants.POLICY_STORE_BUCKET_REQUIRED);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

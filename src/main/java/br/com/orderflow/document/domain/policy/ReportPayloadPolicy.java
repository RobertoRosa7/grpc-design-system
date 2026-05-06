package br.com.orderflow.document.domain.policy;

import br.com.orderflow.document.domain.constant.DomainConstants;
import br.com.orderflow.document.domain.model.DocumentPayload;
import br.com.orderflow.document.domain.model.DocumentType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Policy de validação para payload REPORT.
 */
@Component
@Order(50)
public class ReportPayloadPolicy implements DocumentPayloadPolicy {

    @Override
    public Stream<String> validate(DocumentPayload payload) {
        return Optional.ofNullable(payload.getType())
                .filter(type -> type == DocumentType.REPORT)
                .map(type -> Stream.of(
                        hasText(payload.getReportTitle()) ? null : DomainConstants.POLICY_REPORT_TITLE_REQUIRED,
                        hasText(payload.getReportContent()) ? null : DomainConstants.POLICY_REPORT_CONTENT_REQUIRED)
                        .filter(Objects::nonNull))
                .orElseGet(Stream::empty);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

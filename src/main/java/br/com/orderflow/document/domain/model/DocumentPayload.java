package br.com.orderflow.document.domain.model;

import java.util.List;

/**
 * Value object que encapsula os dados de entrada para geração do documento.
 * Independente do formato de transporte (proto, JSON, etc.).
 */
public class DocumentPayload {

    // Campos comuns
    private final String correlationId;
    private final DocumentType type;

    // Campos específicos por tipo
    private final String customerId;
    private final String orderId;
    private final List<LineItem> items;
    private final double totalAmount;
    private final String currency;

    private final String partyA;
    private final String partyB;
    private final String contractContent;
    private final String validUntil;

    private final String reportTitle;
    private final String period;
    private final String reportContent;

    private DocumentPayload(Builder builder) {
        this.correlationId = builder.correlationId;
        this.type = builder.type;
        this.customerId = builder.customerId;
        this.orderId = builder.orderId;
        this.items = builder.items;
        this.totalAmount = builder.totalAmount;
        this.currency = builder.currency;
        this.partyA = builder.partyA;
        this.partyB = builder.partyB;
        this.contractContent = builder.contractContent;
        this.validUntil = builder.validUntil;
        this.reportTitle = builder.reportTitle;
        this.period = builder.period;
        this.reportContent = builder.reportContent;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public DocumentType getType() {
        return type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPartyA() {
        return partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public String getContractContent() {
        return contractContent;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public String getPeriod() {
        return period;
    }

    public String getReportContent() {
        return reportContent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String correlationId;
        private DocumentType type;
        private String customerId;
        private String orderId;
        private List<LineItem> items;
        private double totalAmount;
        private String currency;
        private String partyA;
        private String partyB;
        private String contractContent;
        private String validUntil;
        private String reportTitle;
        private String period;
        private String reportContent;

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder type(DocumentType type) {
            this.type = type;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder items(List<LineItem> items) {
            this.items = items;
            return this;
        }

        public Builder totalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder partyA(String partyA) {
            this.partyA = partyA;
            return this;
        }

        public Builder partyB(String partyB) {
            this.partyB = partyB;
            return this;
        }

        public Builder contractContent(String contractContent) {
            this.contractContent = contractContent;
            return this;
        }

        public Builder validUntil(String validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        public Builder reportTitle(String reportTitle) {
            this.reportTitle = reportTitle;
            return this;
        }

        public Builder period(String period) {
            this.period = period;
            return this;
        }

        public Builder reportContent(String reportContent) {
            this.reportContent = reportContent;
            return this;
        }

        public DocumentPayload build() {
            return new DocumentPayload(this);
        }
    }

    /**
     * Item de linha de uma nota fiscal — parte do modelo de domínio.
     */
    public record LineItem(String description, int quantity, double unitPrice) {
    }
}

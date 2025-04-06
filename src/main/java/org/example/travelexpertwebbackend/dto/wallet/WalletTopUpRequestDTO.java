package org.example.travelexpertwebbackend.dto.wallet;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WalletTopUpRequestDTO {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    private String description;

    public WalletTopUpRequestDTO() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package org.example.travelexpertwebbackend.dto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class WalletDTO {
    private UUID id;
    private Integer customerId;
    private BigDecimal balance;
    private Instant lastUpdated;

    // Constructors
    public WalletDTO() {
    }

    public WalletDTO(UUID id, Integer customerId, BigDecimal balance, Instant lastUpdated) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


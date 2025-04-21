package org.example.travelexpertwebbackend.dto.wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class WalletTopUpResponseDTO {
    private UUID walletId;
    private BigDecimal balance;
    private Instant lastUpdated;

    public WalletTopUpResponseDTO() {
    }

    public WalletTopUpResponseDTO(UUID walletId, BigDecimal balance, Instant lastUpdated) {
        this.walletId = walletId;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }
}

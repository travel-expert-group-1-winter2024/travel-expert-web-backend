package org.example.travelexpertwebbackend.dto.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletTopUpResponseDTO {
    private UUID walletId;
    private BigDecimal balance;

    public WalletTopUpResponseDTO() {
    }

    public WalletTopUpResponseDTO(UUID walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}

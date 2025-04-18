package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.WalletDTO;
import org.example.travelexpertwebbackend.dto.wallet.WalletTopUpRequestDTO;
import org.example.travelexpertwebbackend.dto.wallet.WalletTopUpResponseDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.entity.Transaction;
import org.example.travelexpertwebbackend.entity.Wallet;
import org.example.travelexpertwebbackend.repository.TransactionRepository;
import org.example.travelexpertwebbackend.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public WalletTopUpResponseDTO topUpWallet(Customer customer, WalletTopUpRequestDTO requestDTO) {
        Wallet wallet = customer.getWallet();
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for customer");
        }

        // top up wallet
        BigDecimal amount = requestDTO.getAmount();
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setLastUpdated(Instant.now());
        Wallet savedWallet = walletRepository.save(wallet);

        // add transaction record
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction.TransactionType.CREDIT); // add money
        transaction.setTransactionDate(Instant.now());
        transaction.setWallet(wallet);
        transaction.setDescription(requestDTO.getDescription());
        transactionRepository.save(transaction);

        return new WalletTopUpResponseDTO(
                savedWallet.getId(),
                savedWallet.getBalance(),
                savedWallet.getLastUpdated()
        );
    }

    public Optional<WalletDTO> getWalletDetails(Integer customerId) {
        return walletRepository.getWalletByCustomerId(customerId)
                .map(wallet -> new WalletDTO(
                        wallet.getId(),
                        wallet.getCustomer().getId(),
                        wallet.getBalance(),
                        wallet.getLastUpdated()
                ));
    }

}

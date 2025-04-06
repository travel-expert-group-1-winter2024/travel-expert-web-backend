package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.entity.CustomerTier;
import org.example.travelexpertwebbackend.entity.Transaction;
import org.example.travelexpertwebbackend.repository.CustomerRepository;
import org.example.travelexpertwebbackend.repository.CustomerTierRepository;
import org.example.travelexpertwebbackend.repository.TransactionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CustomerTierService {

    private final CustomerRepository customerRepository;
    private final CustomerTierRepository customerTierRepository;
    private final TransactionRepository transactionRepository;

    public CustomerTierService(CustomerRepository customerRepository, CustomerTierRepository customerTierRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.customerTierRepository = customerTierRepository;
        this.transactionRepository = transactionRepository;
    }

    public CustomerTier getCustomerTierById(Integer id) {
        return customerTierRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Customer Tier not found"));
    }

    public BigDecimal calculateDiscount(Customer customer, CustomerTier customerTier, BigDecimal totalAmount) {
        BigDecimal discountLimit = customerTier.getDiscountLimit();
        BigDecimal totalDebitAmount = transactionRepository
                .sumAmountByCustomerIdAndType(customer.getId(), Transaction.TransactionType.DEBIT)
                .orElse(BigDecimal.ZERO);

        boolean exceedsLimit = discountLimit != null &&
                totalDebitAmount.subtract(BigDecimal.valueOf(customerTier.getRequiredPoints()))
                        .compareTo(discountLimit) >= 0;

        if (exceedsLimit || customerTier.getDiscountPercentage().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalAmount
                .multiply(customerTier.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public CustomerTier getTierByPoint(Integer point) {
        List<CustomerTier> sortedTiers = customerTierRepository.findAll(Sort.by(Sort.Direction.ASC, "requiredPoints"));

        CustomerTier matchedTier = null;
        for (CustomerTier tier : sortedTiers) {
            if (point >= tier.getRequiredPoints()) {
                matchedTier = tier;
            } else {
                break;
            }
        }

        if (matchedTier == null) {
            throw new IllegalStateException("No matching customer tier found.");
        }

        return matchedTier;
    }

    public CustomerTier getStarterTier() {
        return customerTierRepository.findById(1).orElseThrow(
                () -> new IllegalArgumentException("Start Tier not found"));
    }
}

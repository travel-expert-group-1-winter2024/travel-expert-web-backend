package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.wallet.customer.id = :customerId AND t.transactionType = :type")
    Optional<BigDecimal> sumAmountByCustomerIdAndType(@Param("customerId") Integer customerId, @Param("type") Transaction.TransactionType type);
}

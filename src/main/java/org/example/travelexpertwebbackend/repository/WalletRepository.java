package org.example.travelexpertwebbackend.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("SELECT w FROM Wallet w WHERE w.customer.id = :customerId")
    Optional<Wallet> getWalletByCustomerId(@Param("customerId") Integer customerId);

}

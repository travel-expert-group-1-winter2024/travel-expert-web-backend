package org.example.travelexpertwebbackend.repository.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.travelexpertwebbackend.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameContaining(@Size(max = 50) @NotNull String username);

    Optional<User> findByAgentId(Integer agentId);

    Optional<User> findByCustomer_Id(Integer customerId);
}

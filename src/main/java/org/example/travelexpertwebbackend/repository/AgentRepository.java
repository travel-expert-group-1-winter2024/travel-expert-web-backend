package org.example.travelexpertwebbackend.repository;

import jakarta.validation.constraints.Size;
import org.example.travelexpertwebbackend.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {
    Optional<Agent> findByAgtEmail(@Size(max = 50) String agtEmail);
}

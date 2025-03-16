package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Integer> {
}

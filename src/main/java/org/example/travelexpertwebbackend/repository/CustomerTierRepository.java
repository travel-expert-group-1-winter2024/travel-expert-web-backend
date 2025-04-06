package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.CustomerTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTierRepository extends JpaRepository<CustomerTier, Integer> {
}

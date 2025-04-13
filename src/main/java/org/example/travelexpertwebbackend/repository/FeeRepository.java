package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, String> {
}

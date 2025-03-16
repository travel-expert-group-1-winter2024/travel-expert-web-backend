package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

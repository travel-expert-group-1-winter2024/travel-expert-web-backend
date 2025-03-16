package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}

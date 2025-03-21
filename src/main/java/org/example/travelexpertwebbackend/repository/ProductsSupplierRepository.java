package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.ProductsSupplier;
import org.example.travelexpertwebbackend.entity.TripType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsSupplierRepository extends JpaRepository<ProductsSupplier, String> {
}

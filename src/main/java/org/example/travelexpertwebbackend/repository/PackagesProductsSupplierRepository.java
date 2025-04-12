package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.PackagesProductsSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackagesProductsSupplierRepository extends JpaRepository<PackagesProductsSupplier, Integer> {
    List<PackagesProductsSupplier> findByPackageid_Id(Integer packageidId);
}

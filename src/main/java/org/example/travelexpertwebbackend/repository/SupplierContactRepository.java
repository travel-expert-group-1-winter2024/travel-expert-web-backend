package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.SupplierContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierContactRepository extends JpaRepository<SupplierContact, Integer> {
}

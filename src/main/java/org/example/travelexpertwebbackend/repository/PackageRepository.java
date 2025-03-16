package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Integer> {
}

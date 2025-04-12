package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
}

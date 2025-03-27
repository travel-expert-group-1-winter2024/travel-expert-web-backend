package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.TripType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripTypesRepository extends JpaRepository<TripType, String> {
}

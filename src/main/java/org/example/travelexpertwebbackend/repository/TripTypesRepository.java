package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.TripType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripTypesRepository extends JpaRepository<TripType, String> {
}

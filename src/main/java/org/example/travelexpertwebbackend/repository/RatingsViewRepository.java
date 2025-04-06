package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.RatingsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsViewRepository extends JpaRepository<RatingsView, Integer> {
}

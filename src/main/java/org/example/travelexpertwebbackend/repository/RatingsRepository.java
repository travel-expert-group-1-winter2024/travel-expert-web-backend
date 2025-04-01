package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings, Integer> {
}

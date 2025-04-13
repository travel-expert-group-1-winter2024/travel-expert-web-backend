package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Classes, String> {
}

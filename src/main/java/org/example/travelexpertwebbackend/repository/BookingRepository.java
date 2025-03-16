package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}

package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
}

package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.BookingDetail;
import org.example.travelexpertwebbackend.entity.BookingDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailsViewRepository extends JpaRepository<BookingDetailsView,Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM bookingdetailsview")
    List<BookingDetailsView> findAllBookingDetails();
    List<BookingDetailsView> findByCustomerid(Integer customerid);
}

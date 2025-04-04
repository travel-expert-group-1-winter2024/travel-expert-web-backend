package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.entity.BookingDetailsView;
import org.example.travelexpertwebbackend.repository.BookingDetailsViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingDetailService {
    @Autowired
    private final BookingDetailsViewRepository bookingDetailsRepository;

    public BookingDetailService(BookingDetailsViewRepository bookingDetailsRepository) {
        this.bookingDetailsRepository = bookingDetailsRepository;
    }

    public List<BookingDetailsView> getAllBookingDetails() {
        List<BookingDetailsView> bookingDetailsViewList = bookingDetailsRepository.findAll();
        return bookingDetailsViewList;
    }

    public List<BookingDetailsView> getAllBookingDetailsforCustomer(Integer customerId) {
        return bookingDetailsRepository.findByCustomerid(customerId);
    }
}

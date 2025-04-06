package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.dto.RatingsDTO;
import org.example.travelexpertwebbackend.entity.Customer;
import org.example.travelexpertwebbackend.entity.Package;
import org.example.travelexpertwebbackend.entity.Ratings;
import org.example.travelexpertwebbackend.entity.RatingsView;
import org.example.travelexpertwebbackend.repository.CustomerRepository;
import org.example.travelexpertwebbackend.repository.PackageRepository;
import org.example.travelexpertwebbackend.repository.RatingsRepository;
import org.example.travelexpertwebbackend.repository.RatingsViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingsViewService {

    private final RatingsViewRepository ratingsViewRepository;
    private final PackageRepository packageRepository;
    private final CustomerRepository customerRepository;
    private final RatingsRepository ratingsRepository;


    public RatingsViewService(RatingsViewRepository ratingsViewRepository, PackageRepository packageRepository, CustomerRepository customerRepository, RatingsRepository ratingsRepository) {
        this.ratingsViewRepository = ratingsViewRepository;
        this.packageRepository = packageRepository;
        this.customerRepository = customerRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public List<RatingsView> getAllRatingsWithCustomerInfo() {
        return ratingsViewRepository.findAll();
    }

    public Ratings addRating(RatingsDTO request) {
        Package pkg = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Ratings rating = new Ratings();
        rating.setPackageEntity(pkg);
        rating.setCustomer(customer);
        rating.setRating(request.getRating());
        rating.setComments(request.getComments());
        return ratingsRepository.save(rating);
    }
}

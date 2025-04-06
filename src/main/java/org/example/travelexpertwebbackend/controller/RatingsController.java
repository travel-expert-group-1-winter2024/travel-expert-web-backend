package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.CustomerDTO;
import org.example.travelexpertwebbackend.dto.ErrorInfo;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.RatingsDTO;
import org.example.travelexpertwebbackend.entity.Ratings;
import org.example.travelexpertwebbackend.entity.RatingsView;
import org.example.travelexpertwebbackend.service.RatingsViewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    private final RatingsViewService ratingsViewService;

    public RatingsController(RatingsViewService ratingsViewService) {
        this.ratingsViewService = ratingsViewService;
    }

    @GetMapping
    public List<RatingsView> getAllRatingsInfo() {
        return ratingsViewService.getAllRatingsWithCustomerInfo();
    }

    @PostMapping
    public ResponseEntity<GenericApiResponse<Ratings>> addRatings(@RequestBody RatingsDTO rating){
        try{
            Logger.info("Savings rating for:",rating.getPackageId());
            Ratings saved = ratingsViewService.addRating(rating);
            Logger.info("Ratings Saved for:",rating.getPackageId());
            return ResponseEntity.ok(new GenericApiResponse<>(saved));
        } catch (Exception ex) {
            Logger.error(ex, "Error registering customer: " + rating.getPackageId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponse<>(List.of(new ErrorInfo("An unexpected error occurred"))));
        }
    }
}

package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.PlaceDTO;
import org.example.travelexpertwebbackend.service.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    @Value("${google.places.api.key}")
    private String apiKey;

    @Value("${google.maps.places.url}")
    private String placesApiUrl;

    private final WebClient webClient;

    private final PlacesService placesService;

    public PlacesController(PlacesService placesService, WebClient.Builder webClientBuilder) {
        this.placesService = placesService;
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<String>> searchPlaces(
            @RequestParam String query,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5000") int radius) {

        String url = String.format(
                "%s?query=%s&location=%f,%f&radius=%d&key=%s",
                placesApiUrl,
                query,
                lat,
                lng,
                radius,
                apiKey
        );



        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    // Your existing nearby endpoint
    @GetMapping("/nearby")
    public ResponseEntity<List<PlaceDTO>> getNearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5000") int radius) {
        try {
            return ResponseEntity.ok(
                    placesService.getNearbyTouristAttractions(lat, lng, radius));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

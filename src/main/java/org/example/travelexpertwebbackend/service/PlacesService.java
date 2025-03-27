package org.example.travelexpertwebbackend.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.LatLng;
import org.example.travelexpertwebbackend.dto.PlaceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    public List<PlaceDTO> getNearbyTouristAttractions(double lat, double lng, int radius) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        LatLng location = new LatLng(lat, lng);

        PlacesSearchResult[] results = PlacesApi.nearbySearchQuery(context, location)
                .radius(radius)
                .type(com.google.maps.model.PlaceType.TOURIST_ATTRACTION)
                .await()
                .results;

        return Arrays.stream(results)
                .map(result -> new PlaceDTO(
                        result.name,
                        result.geometry.location.lat,
                        result.geometry.location.lng,
                        result.vicinity
                ))
                .collect(Collectors.toList());
    }

}
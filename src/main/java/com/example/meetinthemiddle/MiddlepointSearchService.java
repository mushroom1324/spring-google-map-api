package com.example.meetinthemiddle;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MiddlepointSearchService {
    private static final int SEARCH_RADIUS_METERS = 5000;  // 검색 반경 설정. 단위는 미터.

    private final GeoApiContext context;

    @Value("${google.api.key}")
    private String apiKey;

    public MiddlepointSearchService() {
        this.context = new GeoApiContext.Builder().apiKey(apiKey).build();
    }

    public List<PlaceDetails> findNearbySubwayStations(GeocodingResponse.Location user1Location, GeocodingResponse.Location user2Location) throws InterruptedException, ApiException, IOException {
        System.out.println("apiKey = " + apiKey);
        double midLatitude = (user1Location.getLat() + user2Location.getLat()) / 2;
        double midLongitude = (user1Location.getLng() + user2Location.getLng()) / 2;

        System.out.println("midLongitude = " + midLongitude);
        System.out.println("midLatitude = " + midLatitude);

        PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, new LatLng(midLatitude, midLongitude))
                .radius(SEARCH_RADIUS_METERS)
                .type(PlaceType.TRANSIT_STATION)
                .await();

        List<PlaceDetails> subwayStations = new ArrayList<>();
        for (PlacesSearchResult result : response.results) {
            PlaceDetails placeDetails = PlacesApi.placeDetails(context, result.placeId).await();
            if (Arrays.asList(placeDetails.types).contains("subway_station")) {
                subwayStations.add(placeDetails);
            }
        }

        return subwayStations;
    }
}
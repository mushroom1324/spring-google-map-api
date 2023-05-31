package com.example.meetinthemiddle.service;

import com.example.meetinthemiddle.model.GeocodingResponse;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@NoArgsConstructor
public class MiddlepointSearchService {
    private static final int SEARCH_RADIUS_METERS = 500;  // 검색 반경 설정. 단위는 미터.


    @Value("${google.api.key}")
    private String apiKey;

    private final GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

    public GeocodingResponse[] findNearbySubwayStations(GeocodingResponse.Location user1Location, GeocodingResponse.Location user2Location) throws InterruptedException, ApiException, IOException {
        System.out.println("apiKey = " + apiKey);
        double midLatitude = (user1Location.getLat() + user2Location.getLat()) / 2;
        double midLongitude = (user1Location.getLng() + user2Location.getLng()) / 2;

        System.out.println("midLongitude = " + midLongitude);
        System.out.println("midLatitude = " + midLatitude);

        PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, new LatLng(midLatitude, midLongitude))
                .radius(SEARCH_RADIUS_METERS)
                .type(PlaceType.TRANSIT_STATION)
                .await();

        for(PlacesSearchResult placesSearchResult : response.results) {
            System.out.println("Name = " + placesSearchResult.name);
            System.out.println("LatLng = " + placesSearchResult.geometry);
        }

        GeocodingResponse[] subwayStations = new GeocodingResponse[response.results.length];

        for (int i = 0; i < response.results.length; i++) {
            subwayStations[i] = new GeocodingResponse(response.results[i].name, response.results[i].geometry);
            subwayStations[i].setGeometry(new GeocodingResponse.Geometry());
            subwayStations[i].getGeometry().setLocation(new GeocodingResponse.Location());
            subwayStations[i].getGeometry().getLocation().setLat(response.results[i].geometry.location.lat);
            subwayStations[i].getGeometry().getLocation().setLng(response.results[i].geometry.location.lng);
        }


        return subwayStations;
    }

    public Map<String, Long> calculateDistancesBetweenNodes(GeocodingResponse user1, GeocodingResponse user2, GeocodingResponse[] subwayStations) throws IOException, InterruptedException, ApiException {
        Map<String, Long> distances = new HashMap<>();

        // Get the user locations
        LatLng user1Location = new LatLng(user1.getGeometry().getLocation().getLat(), user1.getGeometry().getLocation().getLng());
        LatLng user2Location = new LatLng(user2.getGeometry().getLocation().getLat(), user2.getGeometry().getLocation().getLng());

        // Calculate distances between users and each subway station
        for (GeocodingResponse station : subwayStations) {
            LatLng stationLocation = new LatLng(station.getGeometry().getLocation().getLat(), station.getGeometry().getLocation().getLng());

            Long user1ToStation = getTravelTime(user1Location, stationLocation);
            Long user2ToStation = getTravelTime(user2Location, stationLocation);

            distances.put("User1 to " + station.getName(), user1ToStation);
            distances.put("User2 to " + station.getName(), user2ToStation);
        }

        // Calculate distances between each pair of subway stations
        for (int i = 0; i < subwayStations.length; i++) {
            for (int j = i + 1; j < subwayStations.length; j++) {
                LatLng station1Location = new LatLng(subwayStations[i].getGeometry().getLocation().getLat(), subwayStations[i].getGeometry().getLocation().getLng());
                LatLng station2Location = new LatLng(subwayStations[j].getGeometry().getLocation().getLat(), subwayStations[j].getGeometry().getLocation().getLng());

                Long station1ToStation2 = getTravelTime(station1Location, station2Location);

                distances.put(subwayStations[i].getName() + " to " + subwayStations[j].getName(), station1ToStation2);
            }
        }

        // Calculate distance between the two users
        Long user1ToUser2 = getTravelTime(user1Location, user2Location);
        distances.put("User1 to User2", user1ToUser2);

        return distances;
    }

    private Long getTravelTime(LatLng origin, LatLng destination) throws IOException, InterruptedException, ApiException {

        DirectionsResult result = DirectionsApi.newRequest(context)
                .origin(origin)
                .destination(destination)
                .mode(TravelMode.TRANSIT)
                .await();
        if (result.routes != null && result.routes.length > 0) {
            return result.routes[0].legs[0].duration.inSeconds;
        }

        return null;
    }

}
package com.example.meetinthemiddle;

import com.example.meetinthemiddle.model.GeocodingResponse;
import com.example.meetinthemiddle.service.MiddlepointSearchService;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class MiddlepointSearchServiceTest {

    @Autowired
    MiddlepointSearchService middlepointSearchService;

    @Test
    void testFindSubwayStationsBetweenUsers() throws IOException, InterruptedException, ApiException {
        GeocodingResponse user1 = new GeocodingResponse();
        user1.setGeometry(new GeocodingResponse.Geometry(new GeocodingResponse.Location(37.59200, 126.9140)));  // 새절역

        GeocodingResponse user2 = new GeocodingResponse();
        user2.setGeometry(new GeocodingResponse.Geometry(new GeocodingResponse.Location(37.54332, 126.72776)));  // 계산역

        GeocodingResponse[] nearbySubwayStations = middlepointSearchService.findNearbySubwayStations(user1.getGeometry().getLocation(), user2.getGeometry().getLocation());

        Map<String, Long> distancesBetweenNodes = middlepointSearchService.calculateDistancesBetweenNodes(user1, user2, nearbySubwayStations);
        for (Map.Entry<String, Long> entry : distancesBetweenNodes.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }


    }
}
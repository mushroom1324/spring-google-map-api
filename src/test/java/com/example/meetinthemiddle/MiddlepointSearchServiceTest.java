package com.example.meetinthemiddle;

import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MiddlepointSearchServiceTest {

    @Mock
    GeoApiContext geoApiContext;

    @InjectMocks
    MiddlepointSearchService middlepointSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindSubwayStationsBetweenUsers() throws IOException, InterruptedException, ApiException {
        GeocodingResponse user1 = new GeocodingResponse();
        user1.setLocation(new GeocodingResponse.Location(37.59200, 126.9140));  // Seoul

        GeocodingResponse user2 = new GeocodingResponse();
        user2.setLocation(new GeocodingResponse.Location(37.54332, 126.72776));  // Tokyo

//        List<GeocodingResponse> expectedSubwayStations = Arrays.asList(
//                new GeocodingResponse(new GeocodingResponse.Location(36.2048, 138.2529))  // Nagano
//        );

        List<PlaceDetails> nearbySubwayStations = middlepointSearchService.findNearbySubwayStations(user1.getLocation(), user2.getLocation());
        System.out.println("nearbySubwayStations = " + nearbySubwayStations);

//        when(geoApiContext.doSomething(any())).thenReturn(expectedSubwayStations);  // adjust this to match your method call

//        List<GeocodingResponse> actualSubwayStations = middlepointSearchService.findNearbySubwayStations(user1.getLocation(), user2.getLocation());

//        assertEquals(expectedSubwayStations, actualSubwayStations);
    }
}
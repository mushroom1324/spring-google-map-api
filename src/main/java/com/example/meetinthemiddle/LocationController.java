package com.example.meetinthemiddle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LocationController {

    private final GoogleMapsService googleMapsService;

    public LocationController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    @GetMapping("location")
    public GeocodingResponse.Location getLocation(@RequestParam String address) {
        return googleMapsService.getLocation(address);
    }
}
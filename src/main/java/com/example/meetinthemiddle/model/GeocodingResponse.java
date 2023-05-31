package com.example.meetinthemiddle.model;

import com.example.meetinthemiddle.service.GoogleMapsService;
import com.google.maps.model.Geometry;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeocodingResponse {

    private String name;
    private Geometry geometry;


    public GeocodingResponse(String name, com.google.maps.model.Geometry google_geometry) {
        this.name = name;
        this.geometry = new Geometry(new Location(google_geometry.location.lat, google_geometry.location.lng));
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private double lat;
        private double lng;

    }


}

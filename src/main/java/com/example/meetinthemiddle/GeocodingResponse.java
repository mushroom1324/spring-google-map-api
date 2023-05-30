package com.example.meetinthemiddle;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeocodingResponse {

    public GeocodingResponse(Location location) {
        this.location = location;
    }

    private Location location;
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Result {
        private String formatted_address;
        private Geometry geometry;

    }


    public static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
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

    public void setLocation(Location location) {
        this.location = location;
    }
}

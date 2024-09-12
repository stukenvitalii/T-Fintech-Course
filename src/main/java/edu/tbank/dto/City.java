package edu.tbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class City implements Serializable {
    private final String slug;
    private final Coordinates coords;

    @Data
    @AllArgsConstructor
    public static class Coordinates {
        private final double lat;
        private final double lon;
    }
}

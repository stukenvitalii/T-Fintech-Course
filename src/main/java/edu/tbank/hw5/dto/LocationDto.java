package edu.tbank.hw5.dto;

import lombok.Value;

/**
 * DTO for {@link edu.tbank.hw5.entity.Location}
 */
@Value
public class LocationDto {
    String slug;
    String name;
}
package edu.tbank.hw5.dto;

import edu.tbank.hw5.entity.Location;
import lombok.Value;

import java.time.LocalDate;

/**
 * DTO for {@link edu.tbank.hw5.entity.Event}
 */
@Value
public class EventDto {
    String title;
    Double price;
    boolean isFree;
    LocalDate date;
    Location place;
}
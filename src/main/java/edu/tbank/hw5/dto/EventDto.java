package edu.tbank.hw5.dto;

import lombok.Value;

import java.time.LocalDate;

/**
 * DTO for {@link edu.tbank.hw5.entity.Event}
 */
@Value
public class EventDto {
    String title;
    String price;
    boolean isFree;
    LocalDate date;
}
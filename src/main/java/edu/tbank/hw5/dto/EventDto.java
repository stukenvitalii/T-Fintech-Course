package edu.tbank.hw5.dto;

import edu.tbank.hw5.entity.Event;
import lombok.Value;

/**
 * DTO for {@link Event}
 */
@Value
public class EventDto {
    Long id;
    String title;
    Long price;
}
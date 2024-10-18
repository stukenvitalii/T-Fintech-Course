package edu.tbank.hw5.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import edu.tbank.hw5.entity.Event;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EventApiResponse {
    @JsonProperty("results")
    private List<Event> events;
}

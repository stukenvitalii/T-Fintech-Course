package edu.tbank.hw8.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link edu.tbank.hw8.entity.Valute}
 */
public record ValuteDto(@JsonProperty("currency") @NotNull String charCode,
                        @JsonProperty("rate") @NotNull String vunitRate) {
}
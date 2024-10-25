package edu.tbank.hw8.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Error response for 503 Service Unavailable")
public class ServiceUnavailableErrorResponse {
    @Schema(description = "Error message", example = "Service Unavailable")
    private String message;
}
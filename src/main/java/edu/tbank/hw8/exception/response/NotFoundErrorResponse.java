package edu.tbank.hw8.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Error response for 404 Not Found")
public class NotFoundErrorResponse {

    @Schema(description = "Error code", example = "404")
    private int code;

    @Schema(description = "Error message", example = "Not Found")
    private String message;
}

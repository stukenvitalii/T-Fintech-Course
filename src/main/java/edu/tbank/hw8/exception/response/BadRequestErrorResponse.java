package edu.tbank.hw8.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Error response for 400 Bad Request")
public class BadRequestErrorResponse {

    @Schema(description = "Error code", example = "400")
    private int code;

    @Schema(description = "Error message", example = "Bad Request")
    private String message;
}
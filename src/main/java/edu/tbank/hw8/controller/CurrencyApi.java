package edu.tbank.hw8.controller;

import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.exception.response.BadRequestErrorResponse;
import edu.tbank.hw8.exception.response.NotFoundErrorResponse;
import edu.tbank.hw8.exception.response.ServiceUnavailableErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CurrencyApi {

    @Operation(summary = "Get currency rate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValuteDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceUnavailableErrorResponse.class)))
    })
    @GetMapping("/rates/{code}")
    ValuteDto getCurrencyRate(@PathVariable String code);

    @Operation(summary = "Convert currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConvertResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceUnavailableErrorResponse.class)))
    })
    @GetMapping("/convert")
    ConvertResponse convertCurrency(@RequestBody ConvertRequest request);
}
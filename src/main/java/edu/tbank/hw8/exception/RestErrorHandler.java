package edu.tbank.hw8.exception;

import edu.tbank.hw8.exception.response.BadRequestErrorResponse;
import edu.tbank.hw8.exception.response.NotFoundErrorResponse;
import edu.tbank.hw8.exception.response.ServiceUnavailableErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<NotFoundErrorResponse> handleNotFound(HttpClientErrorException.NotFound ex) {
        return new ResponseEntity<>(new NotFoundErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<BadRequestErrorResponse> handleBadRequest(HttpClientErrorException.BadRequest ex) {
        return new ResponseEntity<>(new BadRequestErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BadRequestErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new BadRequestErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ServiceUnavailableErrorResponse> handleServiceUnavailable(HttpServerErrorException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "3600");
        return new ResponseEntity<>(new ServiceUnavailableErrorResponse(ex.getMessage()), headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CurrencyDoesntExistException.class)
    public ResponseEntity<BadRequestErrorResponse> handleCurrencyDoesntExistException(CurrencyDoesntExistException ex) {
        return new ResponseEntity<>(new BadRequestErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<NotFoundErrorResponse> handleCurrencyNotFound(CurrencyNotFoundException ex) {
        return new ResponseEntity<>(new NotFoundErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
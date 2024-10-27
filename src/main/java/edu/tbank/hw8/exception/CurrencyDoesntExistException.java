package edu.tbank.hw8.exception;

public class CurrencyDoesntExistException extends RuntimeException {
    public CurrencyDoesntExistException(String message) {
        super(message);
    }
}

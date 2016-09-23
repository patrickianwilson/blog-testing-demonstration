package com.github.patrickianwilson.template.java.web.service.exceptions;

/**
 * Created by pwilson on 9/23/16.
 */
public class InvalidShorteningRequestException extends RuntimeException {
    public InvalidShorteningRequestException(String message) {
        super(message);
    }

    public InvalidShorteningRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

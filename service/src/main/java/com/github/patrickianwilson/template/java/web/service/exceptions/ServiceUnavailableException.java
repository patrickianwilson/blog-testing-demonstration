package com.github.patrickianwilson.template.java.web.service.exceptions;

/**
 * Created by pwilson on 9/21/16.
 */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.github.patrickianwilson.template.java.web.service.exceptions;

/**
 * Created by pwilson on 9/23/16.
 */
public class RepositoryUnavilableException extends RuntimeException {
    public RepositoryUnavilableException() {
    }

    public RepositoryUnavilableException(String message) {
        super(message);
    }

    public RepositoryUnavilableException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.github.patrickianwilson.template.java.web.service.exceptions;

/**
 * Created by pwilson on 10/6/16.
 */
public class InvalidUrlServiceException extends RuntimeException {
    public InvalidUrlServiceException(String url) {
        super(String.format("The url '%s' is not valid", url));
    }
}

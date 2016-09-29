package com.github.patrickianwilson.template.java.web.shortener;

/**
 * Created by pwilson on 9/29/16.
 */
public class UrlShorteningServiceUnavailableException extends RuntimeException {

    public UrlShorteningServiceUnavailableException() {
    }

    public UrlShorteningServiceUnavailableException(String message) {
        super(message);
    }

    public UrlShorteningServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlShorteningServiceUnavailableException(Throwable cause) {
        super(cause);
    }
}

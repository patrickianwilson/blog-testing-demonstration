package com.github.patrickianwilson.template.java.web.controllers.exceptions;

import javax.ws.rs.core.Response;

/**
 * Created by pwilson on 9/20/16.
 */
public class InvalidURLException extends ErrorCodeApplicationException {

    public InvalidURLException(String url) {
        super(Response.Status.BAD_REQUEST, "invalid_url", String.format("Provided URL [%s] is invalid", url));
    }
}

package com.github.patrickianwilson.template.java.web.controllers.exceptions;

import javax.ws.rs.core.Response;
import com.google.gson.JsonSyntaxException;

/**
 * Created by pwilson on 9/20/16.
 */
public class BadJSONInputException extends ErrorCodeApplicationException {

    public BadJSONInputException(JsonSyntaxException e) {
        super(Response.Status.BAD_REQUEST, "http-bad-json", e.getMessage());
    }
}

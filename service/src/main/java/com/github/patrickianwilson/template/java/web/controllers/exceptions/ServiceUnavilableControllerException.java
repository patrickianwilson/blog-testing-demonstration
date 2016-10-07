package com.github.patrickianwilson.template.java.web.controllers.exceptions;

import javax.ws.rs.core.Response;
import com.google.gson.JsonSyntaxException;

/**
 * Created by pwilson on 10/7/16.
 */
public class ServiceUnavilableControllerException extends ErrorCodeApplicationException {

    public ServiceUnavilableControllerException(Exception e) {
        super(Response.Status.SERVICE_UNAVAILABLE, "http-service-unavailable", e.getMessage());
    }
}

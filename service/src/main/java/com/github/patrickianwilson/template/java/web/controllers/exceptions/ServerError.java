package com.github.patrickianwilson.template.java.web.controllers.exceptions;

import javax.ws.rs.core.Response;

/**
 * Created by pwilson on 9/20/16.
 */
public class ServerError extends ErrorCodeApplicationException {

    public ServerError() {
        super(Response.Status.INTERNAL_SERVER_ERROR);
    }
}

package com.github.patrickianwilson.template.java.web;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.ErrorCodeApplicationException;

/**
 * Created by pwilson on 9/21/16.
 */
@Provider
public class GenericServerErrorExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorCodeApplicationException.Body("Unknown Error", "http-unknown-error"))
                .build();
    }
}

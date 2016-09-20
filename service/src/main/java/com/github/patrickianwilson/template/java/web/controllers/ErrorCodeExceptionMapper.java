package com.github.patrickianwilson.template.java.web.controllers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.ErrorCodeApplicationException;

/**
 * Created by pwilson on 9/20/16.
 */
@Provider
public class ErrorCodeExceptionMapper implements ExceptionMapper<ErrorCodeApplicationException> {

    @Override
    public Response toResponse(ErrorCodeApplicationException exception) {
        return Response.status(exception.getStatus()).entity(exception.getBody()).build();
    }
}

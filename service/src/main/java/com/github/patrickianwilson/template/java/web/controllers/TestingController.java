package com.github.patrickianwilson.template.java.web.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by pwilson on 9/20/16.
 */
@Path("/testing")
public class TestingController {

    @GET
    public Response testAssumptions(@QueryParam("url") String param) {
        return Response.ok().build();
    }
}

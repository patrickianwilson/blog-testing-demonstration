package com.github.patrickianwilson.template.java.web.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.InvalidURLException;

/**
 * Created by pwilson on 9/20/16.
 */
@Path("/status")
public class StatusController {

    @GET
    public Response getStatus() {
        throw new InvalidURLException("http://f.yu/13279127391273!!!!?????");
//        return Response.ok("I am doing awesome!").build();
    }
}

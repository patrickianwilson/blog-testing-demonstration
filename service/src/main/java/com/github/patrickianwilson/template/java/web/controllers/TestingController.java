package com.github.patrickianwilson.template.java.web.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.BadJSONInputException;
import com.google.gson.JsonSyntaxException;

/**
 * Created by pwilson on 9/20/16.
 */
@Path("/testing")
public class TestingController {

    @GET
    public Response testAssumptions(@QueryParam("url") String param, @QueryParam("error") String errorCond) {
        if (errorCond != null) {
            switch (errorCond) {
                case "badReq":
                    throw new BadJSONInputException(new JsonSyntaxException("fake bad json"));
                default:
                    throw new RuntimeException("Sneaky runtuime exception...  IE - one that wasn't expected.");
            }
        }

        return Response.ok()
                .header(HttpHeaders.LOCATION, "http://g.og/shortened")
                .header("X-Testing-Orignal-URL", param)
                .build();
    }
}

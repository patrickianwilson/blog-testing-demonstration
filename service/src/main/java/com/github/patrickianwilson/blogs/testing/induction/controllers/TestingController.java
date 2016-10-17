package com.github.patrickianwilson.blogs.testing.induction.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import com.github.patrickianwilson.blogs.testing.induction.controllers.exceptions.BadJSONInputException;
import com.google.gson.JsonSyntaxException;

/*
 The MIT License (MIT)

 Copyright (c) 2014 Patrick Wilson

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
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

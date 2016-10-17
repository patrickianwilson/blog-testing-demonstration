package com.github.patrickianwilson.blogs.testing.induction.controllers.exceptions;

import java.util.Map;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import com.google.common.collect.ImmutableMap;

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
public abstract class ErrorCodeApplicationException extends RuntimeException {

    final private Body body;
    final private Response.Status status;

    public ErrorCodeApplicationException(Response.Status status) {
        if (defaultErrorBodies.containsKey(status)) {
            body = defaultErrorBodies.get(status);
        } else if (defaultFamilyErrorBodies.containsKey(status.getFamily())) {
            body = defaultFamilyErrorBodies.get(status.getFamily());
        } else {
            body = null;
        }

        this.status = status;
    }

    public ErrorCodeApplicationException(Response.Status status, String code, String message) {
        body = new Body(message, code);
        this.status = status;
    }

    public static class Body {

        private String message;
        private String code;

        public Body(String message, String code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


    public Response.Status getStatus() {
        return status;
    }

    public Body getBody() {
        return body;
    }

    private Map<Response.Status, Body> defaultErrorBodies = ImmutableMap.<Response.Status, Body>builder()
            .put(Response.Status.UNAUTHORIZED, new Body("The request requires authentication but no Authentication header was provided.", "http-no-auth"))
            .build();

    private Map<Response.Status.Family, Body> defaultFamilyErrorBodies = ImmutableMap.<Response.Status.Family, Body>builder()
            .put(Response.Status.Family.CLIENT_ERROR, new Body("This request was malformed", "http-bad-request"))
            .put(Response.Status.Family.SERVER_ERROR, new Body("The request was interrupted by an unknown error, please try again later", "http-serv-error"))
            .build();
}

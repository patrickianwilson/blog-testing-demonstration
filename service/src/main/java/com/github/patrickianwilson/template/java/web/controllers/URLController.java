package com.github.patrickianwilson.template.java.web.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.InvalidURLException;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.ServerError;
import com.github.patrickianwilson.template.java.web.controllers.exceptions.ServiceUnavilableControllerException;
import com.github.patrickianwilson.template.java.web.service.URLService;
import com.github.patrickianwilson.template.java.web.service.exceptions.InvalidUrlServiceException;
import com.github.patrickianwilson.template.java.web.service.exceptions.RepositoryUnavilableException;
import com.github.patrickianwilson.template.java.web.service.exceptions.ServiceUnavailableException;

/**
 * Created by pwilson on 9/20/16.
 */
@Path("/shortened")
public class URLController {

    private URLService urlService;

    public URLController(URLService urlService) {
        this.urlService = urlService;
    }

    @GET
    public Response shortenUrl(@QueryParam("url") String url) {
        try {
            String shortUrl = urlService.shorten(url);

            return Response.ok()
                    .header(HttpHeaders.LOCATION, shortUrl)
                    .header("X-Testing-Orignal-URL", url)
                    .build();
        } catch (ServiceUnavailableException | RepositoryUnavilableException e) {
            throw new ServiceUnavilableControllerException(e);
        } catch (InvalidUrlServiceException e) {
            throw new InvalidURLException(url);
        } catch (Throwable t) {
            throw new ServerError();
        }

    }
}

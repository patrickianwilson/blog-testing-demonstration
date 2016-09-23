package com.github.patrickianwilson.template.java.web.service;

import java.net.URI;
import java.net.URL;
import com.github.patrickianwilson.template.java.web.service.exceptions.ServiceUnavailableException;

/**
 * Created by pwilson on 9/20/16.
 */
public interface URLService {

    URI shorten(URI longUrl) throws ServiceUnavailableException;
}

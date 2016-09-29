package com.github.patrickianwilson.template.java.web.service;

import java.net.URI;
import com.github.patrickianwilson.template.java.web.repositories.URLRepository;
import com.github.patrickianwilson.template.java.web.shortener.URLShortenerAdaptor;
import com.google.inject.Inject;

/**
 * Created by pwilson on 9/20/16.
 */
public class SimpleURLService implements URLService {

    private URLRepository databaseRepo;
    private URLShortenerAdaptor shortenerAdaptor;

    @Inject
    public SimpleURLService(URLRepository databaseRepo, URLShortenerAdaptor shortenerAdaptor) {
        this.databaseRepo = databaseRepo;
        this.shortenerAdaptor = shortenerAdaptor;
    }

    public String shorten(String longUrl) {

        return null;
    }
}

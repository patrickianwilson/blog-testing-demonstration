package com.github.patrickianwilson.template.java.web.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import com.github.patrickianwilson.template.java.web.repositories.URLRepository;
import com.github.patrickianwilson.template.java.web.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.template.java.web.service.exceptions.InvalidUrlServiceException;
import com.github.patrickianwilson.template.java.web.service.exceptions.RepositoryUnavilableException;
import com.github.patrickianwilson.template.java.web.service.exceptions.ServiceUnavailableException;
import com.github.patrickianwilson.template.java.web.service.exceptions.UnknownServiceException;
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

    /**
     * return a short url for the provided long url.
     *
     * Algo:  first lookup the long url to see if it has already been shortened and only request a new shortened url if
     * no cached entry can be found.
     * @param longUrl
     * @return
     */
    public String shorten(String longUrl) {

        validate(longUrl);

        try {
            UrlServiceEntity entity = databaseRepo.findByLongForm(longUrl);

            if (entity == null) {
                String shortUrl = shortenerAdaptor.shortenUrl(longUrl);
                entity = new UrlServiceEntity(longUrl, shortUrl);
                databaseRepo.persist(entity);
            }

            return entity.getShortForm();
        } catch (ServiceUnavailableException | RepositoryUnavilableException e) {
            throw e;
        } catch (Exception e) {
            throw new UnknownServiceException(e);
        }
    }

    private void validate(String longUrl) throws InvalidUrlServiceException {
        try {
            new URL(longUrl);
        } catch (MalformedURLException e) {
            throw new InvalidUrlServiceException(longUrl);
        }
    }
}

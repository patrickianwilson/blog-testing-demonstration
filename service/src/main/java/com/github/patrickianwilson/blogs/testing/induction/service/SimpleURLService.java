package com.github.patrickianwilson.blogs.testing.induction.service;

import java.net.MalformedURLException;
import java.net.URL;
import com.github.patrickianwilson.blogs.testing.induction.repositories.URLRepository;
import com.github.patrickianwilson.blogs.testing.induction.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.InvalidUrlServiceException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.RepositoryUnavilableException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.ServiceUnavailableException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.UnknownServiceException;
import com.github.patrickianwilson.blogs.testing.induction.shortener.URLShortenerAdaptor;
import com.google.inject.Inject;

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

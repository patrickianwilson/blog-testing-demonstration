package com.github.patrickianwilson.blogs.testing.induction.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import com.github.patrickianwilson.blogs.testing.induction.repositories.URLRepository;
import com.github.patrickianwilson.blogs.testing.induction.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.InvalidUrlServiceException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.RepositoryUnavilableException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.ServiceUnavailableException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.UnknownServiceException;
import com.github.patrickianwilson.blogs.testing.induction.shortener.URLShortenerAdaptor;

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


/**
 * Service Layer Test.  Induction Step style test that only leverages mock versions of internal components.
 *
 * Test Plan:
 * 1. Verify the success path with not previously shortened url
 * 2. Verify the success path with previously shortened url
 * 3. Verify an invalid url (bad input status)
 * 4. Fail the Shortening service component (Service unavailable)
 * 5. Fail the DB Repo (Service unavailable)
 * 6. Throw a sneaky runtime from one of the internal components and ensure it passes through to the controller layer.
 *
 */
public class SimpleUrlServiceTests {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    public static final String VALID_URL = "https://www.google.com/";
    public static final String SHORT_URL_FOR_VALID_URL = "http://go.og/sdasif";
    public static final String INVALID_URL = ".";

    private URLShortenerAdaptor mockShortener = context.mock(URLShortenerAdaptor.class);
    private URLRepository mockUrlRepo = context.mock(URLRepository.class);

    private SimpleURLService underTest = new SimpleURLService(mockUrlRepo, mockShortener);

    @Test
    public void verifySuccessPathWithNotPreviouslyShortenedUrl() {

        context.checking(new Expectations() {{
            oneOf(mockUrlRepo).findByLongForm(VALID_URL);
                will(returnValue(null));

            oneOf(mockShortener).shortenUrl(VALID_URL);
                will(returnValue(SHORT_URL_FOR_VALID_URL));


            oneOf(mockUrlRepo).persist(new UrlServiceEntity(VALID_URL, SHORT_URL_FOR_VALID_URL));
        }});

        String actual = underTest.shorten(VALID_URL);
        assertThat("Incorrect Short URL created.", actual, is(equalTo(SHORT_URL_FOR_VALID_URL)));

    }

    @Test
    public void verifySuccessPathWithPreviouslyShortenedUrl() {

        context.checking(new Expectations() {{
            oneOf(mockUrlRepo).findByLongForm(VALID_URL);
            will(returnValue(new UrlServiceEntity(VALID_URL, SHORT_URL_FOR_VALID_URL)));

            never(mockShortener).shortenUrl(VALID_URL);

            never(mockUrlRepo).persist(new UrlServiceEntity(VALID_URL, SHORT_URL_FOR_VALID_URL));

        }});

        String actual = underTest.shorten(VALID_URL);
        assertThat("Incorrect Short URL created.", actual, is(equalTo(SHORT_URL_FOR_VALID_URL)));

    }

    @Test(expected = InvalidUrlServiceException.class)
    public void doesItThrowBadInputExceptionWithInvalidUrl() {
        context.checking(new Expectations() {{
            never(mockUrlRepo).findByLongForm(VALID_URL);

            never(mockShortener).shortenUrl(VALID_URL);

            never(mockUrlRepo).persist(new UrlServiceEntity(VALID_URL, SHORT_URL_FOR_VALID_URL));

        }});

        underTest.shorten(INVALID_URL);
    }

    @Test(expected = RepositoryUnavilableException.class)
    public void doesItThrowRepositoryUnavailableExceptionWithRepositoryFailure() {

        context.checking(new Expectations() {{
            oneOf(mockUrlRepo).findByLongForm(VALID_URL);
                will(throwException(new RepositoryUnavilableException("Mock Exception")));
        }});

        String actual = underTest.shorten(VALID_URL);

    }

    @Test(expected = ServiceUnavailableException.class)
    public void doesItThrowServiceUnavailableExceptionWithShortenerServiceFailure() {

        context.checking(new Expectations() {{
            oneOf(mockUrlRepo).findByLongForm(VALID_URL);
                will(returnValue(null));

            oneOf(mockShortener).shortenUrl(VALID_URL);
                will(throwException(new ServiceUnavailableException("Mock Exception From Shortener Adaptor")));
        }});

        underTest.shorten(VALID_URL);

    }

    @Test(expected = UnknownServiceException.class)
    public void doesItThrowUnknownServiceExceptionWithSneakyRuntimeFailure() {

        context.checking(new Expectations() {{
            oneOf(mockUrlRepo).findByLongForm(VALID_URL);
            will(throwException(new RuntimeException("Sneaky!!")));
        }});

        underTest.shorten(VALID_URL);

    }

}

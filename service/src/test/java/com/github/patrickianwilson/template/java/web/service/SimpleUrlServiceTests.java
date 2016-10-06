package com.github.patrickianwilson.template.java.web.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.github.patrickianwilson.template.java.web.repositories.URLRepository;
import com.github.patrickianwilson.template.java.web.repositories.entities.UrlServiceEntity;
import com.github.patrickianwilson.template.java.web.service.exceptions.InvalidUrlServiceException;
import com.github.patrickianwilson.template.java.web.service.exceptions.RepositoryUnavilableException;
import com.github.patrickianwilson.template.java.web.service.exceptions.ServiceUnavailableException;
import com.github.patrickianwilson.template.java.web.service.exceptions.UnknownServiceException;
import com.github.patrickianwilson.template.java.web.shortener.URLShortenerAdaptor;

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

package com.github.patrickianwilson.blogs.testing.induction;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import com.github.patrickianwilson.blogs.testing.induction.controllers.URLController;
import com.github.patrickianwilson.blogs.testing.induction.controllers.exceptions.ServiceUnavilableControllerException;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.ServiceUnavailableException;
import com.github.patrickianwilson.blogs.testing.induction.controllers.exceptions.InvalidURLException;
import com.github.patrickianwilson.blogs.testing.induction.controllers.exceptions.ServerError;
import com.github.patrickianwilson.blogs.testing.induction.service.URLService;
import com.github.patrickianwilson.blogs.testing.induction.service.exceptions.InvalidUrlServiceException;

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
 * Also an induction step test.  Relies on /integration-tests:JaxRsIntegrationRunner as a base case - thus we can assume the JAX-RS annotations
 * and Resteasy are working as expected.
 *
 * Test plan:
 * 1. verify steel thread - valid url comes in and is passed to the service
 * 2. service internal exception mapping to 500 status
 * 3. bad url exception map to 400 status
 * 4. *unavilavaible class of exceptions map to 503 status
 */
public class URLControllerTests {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    public static final String VALID_URL = "https://www.google.com/";
    public static final String SHORT_URL_FOR_VALID_URL = "http://go.og/sdasif";
    public static final String INVALID_URL = ".";


    private URLService mockUrlService = context.mock(URLService.class);

    private URLController underTest = new URLController(mockUrlService);

    @Test
    public void verifySteelThread() {

        context.checking(new Expectations() {{
            oneOf(mockUrlService).shorten(VALID_URL);
                will(returnValue(SHORT_URL_FOR_VALID_URL));
        }});

        Response actual = underTest.shortenUrl(VALID_URL);

        assertThat("Response should never be null", actual, is(notNullValue()));
        assertThat("Wrong Status Code For Success", actual.getStatus(), is(200));
    }

    @Test(expected = ServerError.class)
    public void shouldGenerateInternalServerException() {
        context.checking(new Expectations() {{
            oneOf(mockUrlService).shorten(VALID_URL);
            will(throwException(new RuntimeException("sneaky exception")));
        }});

        underTest.shortenUrl(VALID_URL);
    }

    @Test(expected = InvalidURLException.class)
    public void badInputShouldGenerateBadRequestException() {
        context.checking(new Expectations() {{
            oneOf(mockUrlService).shorten(INVALID_URL);
            will(throwException(new InvalidUrlServiceException(INVALID_URL)));
        }});

        underTest.shortenUrl(INVALID_URL);
    }


    @Test(expected = ServiceUnavilableControllerException.class)
    public void serviceUnavailableExceptionIsHandled() {
        context.checking(new Expectations() {{
            oneOf(mockUrlService).shorten(VALID_URL);
            will(throwException(new ServiceUnavailableException("mock service is not available")));
        }});

        underTest.shortenUrl(VALID_URL);
    }

}

package com.github.patrickianwilson.template.java.web;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.github.patrickianwilson.template.java.web.controllers.URLController;
import com.github.patrickianwilson.template.java.web.service.URLService;

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
}

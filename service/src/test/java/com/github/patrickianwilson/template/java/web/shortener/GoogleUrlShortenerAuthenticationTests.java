package com.github.patrickianwilson.template.java.web.shortener;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test Plan:
 *
 * Ensure the adaptor handles bad authentications properly.
 *
 * 1. Initialize an adaptor with a bogus key and verify the adaptor throws the "unavailable" exception.
 */
public class GoogleUrlShortenerAuthenticationTests {

    GoogleURLShortenerAdaptor underTest = new GoogleURLShortenerAdaptor("AIzbogus_Api_keyhere");

    @Test(expected = UrlShorteningServiceUnavailableException.class)
    public void withBadAuthentication() {
        underTest.shortenUrl("http://itdoesntmatter.com/");
        fail("Should have encountered an ServiceUnavailableException by now...");
    }
}

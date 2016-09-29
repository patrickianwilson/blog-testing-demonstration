package com.github.patrickianwilson.template.java.web.shortener;

import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static com.github.patrickianwilson.template.java.web.shortener.GoogleUrlShortenerAdaptorTests.ShortUrlMatcher.expandsTo;

/**
 * Test Plan:
 *
 * 1. shorten a standard url and verify the shortened works.
 */
public class GoogleUrlShortenerAdaptorTests {

    public static final String LONG_URL = "http://github.com/patrickianwilson";
    GoogleURLShortenerAdaptor underTest;

    @Before
    public void initializeAdaptorWithApiKey() {
        String apiKeyFromProperties = System.getProperty("G_API_KEY");

        if (apiKeyFromProperties == null) {
            throw new RuntimeException("Test Configuration Exception! Must run this test with a system property specifying the Google API key for the URL Shortener API [G_API_KEY].  Visit https://developers.google.com/url-shortener/v1/getting_started#shorten");
        }
        underTest = new GoogleURLShortenerAdaptor(apiKeyFromProperties);
    }

    @Test
    public void withAGithubProjectPage() {
        String shortUrl = underTest.shortenUrl(LONG_URL);

        assertNotNull("Whoops, adaptor returned a null URL.", shortUrl);
        assertThat("Short URL doesn't redirect to the expected page", shortUrl, expandsTo(LONG_URL));
    }

    public static class ShortUrlMatcher extends BaseMatcher<String> {
        CloseableHttpClient httpClient = HttpClients.custom().disableRedirectHandling().build();

        private String longUrl;

        public ShortUrlMatcher(String longUrl) {
            this.longUrl = longUrl;
        }

        @Override
        public boolean matches(Object item) {
            if (!(item instanceof String)) {
                return false;
            }

            String shortUrl = (String) item;
            HttpGet expandReq = new HttpGet(shortUrl);
            try (CloseableHttpResponse resp = httpClient.execute(expandReq)) {
                Header locationHeader = resp.getFirstHeader("Location");
                if (locationHeader == null) {
                    //likely this was not a shortened URL.
                    throw new RuntimeException("The short url " + shortUrl + " didn't respond with a location header - likely not a url shortener service url");
                }

                return longUrl.equals(locationHeader.getValue());
            } catch (IOException e) {
                throw new RuntimeException("the Short URL [" + shortUrl + "] is not accessible", e);
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a shortened url that points to ").appendValue(longUrl);
        }

        public static ShortUrlMatcher expandsTo(String longUrl) {
            return new ShortUrlMatcher(longUrl);
        }
    }
}

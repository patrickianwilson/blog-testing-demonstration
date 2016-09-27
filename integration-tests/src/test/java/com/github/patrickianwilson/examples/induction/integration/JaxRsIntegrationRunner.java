package com.github.patrickianwilson.examples.induction.integration;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.common.base.Preconditions;

/**
 * JAX-RS Integration Test.
 *
 * Testing Plan:
 * 1. Verify that methods annotated with @GET are mapped to the controller.
 * 2. Verify that params are parsed using the @QueryParam are actually the values on the URL using the ?queryparamkey=value
 * 3. Verify the exception mapping stuff is well understood
 *     a. pass a flag parameter that instructs the controller to throw a mapped exception - then verify we see the status + body we expect.
 */
public class JaxRsIntegrationRunner {
    private static Runner runner = null;

    @BeforeClass
    public static void startServer() throws Exception {
        Preconditions.checkNotNull(System.getProperty("WAR_FILE"), "This test requires the web path for the service project!  Run with System Property \"WAR_FILE\"");

        runner = new Runner(System.getProperty("WAR_FILE"));

        runner.start();

    }



    @Test
    public void verifyServiceHealthy() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet getStatus = new HttpGet("http://localhost:8080/status");

        CloseableHttpResponse statusResp = client.execute(getStatus);

        Assert.assertThat("Non 200 status response received", statusResp.getStatusLine().getStatusCode(), is(200));

    }

    @Test
    public void verifyJaxRsGETAndParamsWorkAsAssumed() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet getStatus = new HttpGet("http://localhost:8080/testing?url=http://input.com/long/url");

        CloseableHttpResponse statusResp = client.execute(getStatus);

        Assert.assertThat("Non 200 status response received", statusResp.getStatusLine().getStatusCode(), is(200));
        Assert.assertThat("X-Testing-Orignal-URL header was not present in the response", statusResp.getFirstHeader("X-Testing-Orignal-URL").getValue(), is ("http://input.com/long/url"));
        Assert.assertThat("Location header was not present in the response", statusResp.getFirstHeader("Location").getValue(), is ("http://g.og/shortened"));
    }

    @Test
    public void verifyJaxRsRuntimeExceptionMappingWorks() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet getStatus = new HttpGet("http://localhost:8080/testing?error");

        CloseableHttpResponse statusResp = client.execute(getStatus);

        Assert.assertThat("A 500 response was expected", statusResp.getStatusLine().getStatusCode(), is(500));
    }

    @Test
    public void verifyJaxRsBadRequestExceptionMappingWorks() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet getStatus = new HttpGet("http://localhost:8080/testing?error=badReq");

        CloseableHttpResponse statusResp = client.execute(getStatus);

        Assert.assertThat("A 500 response was expected", statusResp.getStatusLine().getStatusCode(), is(400));
    }

    @AfterClass
    public static void stopServer() throws Exception {
        runner.stop();
    }



}

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
 * Created by pwilson on 9/26/16.
 */
public class JaxRsIntegrationRunner {
    private static Server server;

    @BeforeClass
    public static void startServer() throws Exception {
        Preconditions.checkNotNull(System.getProperty("WAR_FILE"), "This test requires the web path for the service project!  Run with System Property \"WAR_FILE\"");

        Runner runner = new Runner(System.getProperty("WAR_FILE"));
        server = runner.start();
//        tomcat.getServer().await();
    }



    @Test
    public void verifyServiceHealthy() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet getStatus = new HttpGet("http://localhost:8080/status");

        CloseableHttpResponse statusResp = client.execute(getStatus);

        Assert.assertThat("Non 200 status response received", statusResp.getStatusLine().getStatusCode(), is(200));
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }



}

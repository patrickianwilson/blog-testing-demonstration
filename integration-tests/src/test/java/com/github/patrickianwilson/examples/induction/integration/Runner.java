package com.github.patrickianwilson.examples.induction.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by pwilson on 9/26/16.
 */
public class Runner {

    public static final int DEFAULT_PORT = 8080;
    private final String warFile;
    private final Server jetty;

    public Runner(String webDir) {
        this.warFile = webDir;
        jetty = new Server(DEFAULT_PORT);
    }

    public void start() throws Exception {

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(warFile);

        jetty.setHandler(webapp);
        jetty.start();

    }

    public void stop() throws Exception {
        jetty.stop();
    }

    /**
     * Join the main thread to the jetty worker pool and block until the pool shuts down.
     * @throws Exception
     */
    public void join() throws Exception {

    }
}

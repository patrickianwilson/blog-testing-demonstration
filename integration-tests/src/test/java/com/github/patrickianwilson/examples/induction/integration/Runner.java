package com.github.patrickianwilson.examples.induction.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by pwilson on 9/26/16.
 */
public class Runner {

    public static final int DEFAULT_PORT = 8080;
    private final String warFile;

    public Runner(String webDir) {
        this.warFile = webDir;
    }

    public Server start() throws Exception {


        Server jetty = new Server(DEFAULT_PORT);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(warFile);

        jetty.setHandler(webapp);
        jetty.start();
        return jetty;
    }
}

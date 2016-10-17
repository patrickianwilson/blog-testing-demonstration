package com.github.patrickianwilson.blogs.testing.induction.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Runner {
    public static final Logger LOG = LoggerFactory.getLogger(Runner.class);
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

        LOG.info("Jetty Status is {}", jetty.getState());
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

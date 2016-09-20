package com.github.patrickianwilson.template.java.web;

import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import com.github.patrickianwilson.template.java.web.controllers.ErrorCodeExceptionMapper;
import com.github.patrickianwilson.template.java.web.controllers.StatusController;
import com.google.inject.servlet.ServletModule;

/**
 * Created by pwilson on 3/7/16.
 */
public class WebModule extends ServletModule {

    @Override
    protected void configureServlets() {
        super.configureServlets();

        //message handlers
        bind(JsonMessageBodyHandler.class);

        //exception mappers
        bind(ErrorCodeExceptionMapper.class);

        //controllers.
        bind(StatusController.class);

        //boot up the resteasy dispatcher.
        bind(FilterDispatcher.class).asEagerSingleton();
        filter("/*").through(FilterDispatcher.class);

    }
}

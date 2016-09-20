package com.github.patrickianwilson.template.java.web;

import java.util.List;
import javax.servlet.ServletContext;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Created by pwilson on 3/7/16.
 */
public class ApplicationContextListener extends GuiceResteasyBootstrapServletContextListener {


    @Override
    protected List<Module> getModules(ServletContext context) {
        return Lists.newArrayList((Module) new WebModule());
    }


}

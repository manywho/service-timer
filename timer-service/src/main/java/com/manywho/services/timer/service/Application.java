package com.manywho.services.timer.service;

import com.manywho.sdk.services.BaseApplication;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.ee.servlet.QuartzInitializerServlet;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

@ApplicationPath("/")
public class Application extends BaseApplication {
    @Context
    ServletContext servletContext;

    public Application() {
//        servletContext.setInitParameter("quartz:shutdown-on-unload", "true");
//        servletContext.setInitParameter("quartz:wait-on-shutdown", "false");
//        servletContext.setInitParameter("quartz:start-scheduler-on-load", "true");
        registerSdk().packages("com.manywho.services.timer")
                .register(new ApplicationBinder());



//        servletContext.addServlet("QuartzInitializer", QuartzInitializerServlet.class);
    }
}

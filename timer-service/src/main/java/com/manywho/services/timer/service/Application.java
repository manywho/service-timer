package com.manywho.services.timer.service;

import com.manywho.sdk.services.BaseApplication;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

@ApplicationPath("/")
public class Application extends BaseApplication {
    @Context
    ServletContext servletContext;

    public Application() {
        registerSdk().packages("com.manywho.services.timer")
                .register(new ApplicationBinder());
    }
}

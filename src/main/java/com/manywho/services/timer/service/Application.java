package com.manywho.services.timer.service;

import com.manywho.sdk.services.BaseApplication;
import com.manywho.services.timer.service.quartz.QuartzSchedulerFactory;
import org.glassfish.hk2.utilities.Binder;
import org.quartz.SchedulerException;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class Application extends BaseApplication {
    static {
        // Redirect any calls to JUL to SLF4j (..Jersey WTF?)
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    public Application() {
        this(new ApplicationBinder());

        try {
            new QuartzSchedulerFactory()
                    .provide()
                    .start();
        } catch (SchedulerException e) {
            throw new RuntimeException("Unable to start the scheduler", e);
        }
    }

    public Application(Binder binder) {
        registerSdk().packages("com.manywho.services.timer")
                .register(binder);
    }

    public static void main(String[] args) {
        startServer(new Application(), "api/timer/1");
    }
}

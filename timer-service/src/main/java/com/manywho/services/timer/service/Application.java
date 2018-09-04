package com.manywho.services.timer.service;

import com.manywho.sdk.services.BaseApplication;
import org.glassfish.hk2.utilities.Binder;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class Application extends BaseApplication {

    public Application() {
        this(new ApplicationBinder());
    }

    public Application(Binder binder) {
        // Redirect any calls to JUL to SLF4j (..Jersey WTF?)
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        registerSdk().packages("com.manywho.services.timer")
                .register(binder);
    }

    public static void main(String[] args) {
        startServer(new Application(), "api/timer/1");
    }
}

package com.manywho.services.timer.service.stubs;

import org.glassfish.hk2.api.Factory;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestFactory implements Factory<HttpServletRequest> {
    @Override
    public HttpServletRequest provide() {
        return new DummyHttpServletRequest();
    }

    @Override
    public void dispose(HttpServletRequest httpServletRequest) {
        
    }
}

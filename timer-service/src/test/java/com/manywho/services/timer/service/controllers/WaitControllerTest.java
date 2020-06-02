package com.manywho.services.timer.service.controllers;

import com.manywho.sdk.entities.run.EngineValue;
import com.manywho.sdk.entities.run.EngineValueCollection;
import com.manywho.sdk.entities.run.ServiceProblem;
import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.providers.ObjectMapperProvider;
import com.manywho.sdk.test.FunctionalTest;
import com.manywho.sdk.utils.AuthorizationUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.UUID;

public class WaitControllerTest extends FunctionalTest {

    @Override
    protected Application configure() {
        enable(TestProperties.DUMP_ENTITY);
        enable(TestProperties.LOG_TRAFFIC);

        return new com.manywho.services.timer.service.Application();
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(ObjectMapperProvider.class);
    }

    @Test
    public void testAbsolute() throws Exception {
        MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
        headers.add("Authorization", AuthorizationUtils.serialize(getDefaultAuthenticatedWho()));

        String randomToken = UUID.randomUUID().toString();

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.DateTime, "2025-09-04T00:00:00.0000000+01:00"));
            }});
            setToken(randomToken);
        }};

        ServiceResponse response = target("wait/absolute")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .post(Entity.json(serviceRequest), ServiceResponse.class);

        Assert.assertEquals(InvokeType.Wait, response.getInvokeType());
        Assert.assertEquals(randomToken, response.getToken());
    }

    @Test
    public void testAbsoluteWithInvalidSchedule() throws Exception {
        MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
        headers.add("Authorization", AuthorizationUtils.serialize(getDefaultAuthenticatedWho()));

        String randomToken = UUID.randomUUID().toString();
        final String ENDPOINT = "/wait/absolute";

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.DateTime, "1990-09-04T00:00:00.0000000+01:00"));
            }});
            setToken(randomToken);
        }};

        Response response = target(ENDPOINT)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .buildPost(Entity.json(serviceRequest))
                .invoke();


        ServiceProblem serviceResponse = response.readEntity(ServiceProblem.class);
        Assert.assertTrue(serviceResponse.getUri().equalsIgnoreCase(ENDPOINT));
        Assert.assertTrue(serviceResponse.getMessage().contains("Validation errors"));
    }

    @Test
    public void testRelative() throws Exception {
        MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
        headers.add("Authorization", AuthorizationUtils.serialize(getDefaultAuthenticatedWho()));

        String randomToken = UUID.randomUUID().toString();

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.String, "in 140 seconds"));
            }});
            setToken(randomToken);
        }};

        ServiceResponse response = target("wait/relative")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .post(Entity.json(serviceRequest), ServiceResponse.class);

        Assert.assertEquals(InvokeType.Wait, response.getInvokeType());
        Assert.assertEquals(randomToken, response.getToken());
    }

    @Test
    public void testRelativeWithInvalidSchedule() throws Exception {
        MultivaluedMap<String,Object> headers = new MultivaluedHashMap<>();
        headers.add("Authorization", AuthorizationUtils.serialize(getDefaultAuthenticatedWho()));

        String randomToken = UUID.randomUUID().toString();
        final String ENDPOINT = "/wait/relative";
        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.String, "THIS ISN'T A DATE, WUT"));
            }});
            setToken(randomToken);
        }};

        Response response = target(ENDPOINT)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .buildPost(Entity.json(serviceRequest))
                .invoke();

        ServiceProblem serviceResponse = response.readEntity(ServiceProblem.class);
        Assert.assertTrue(serviceResponse.getUri().equalsIgnoreCase(ENDPOINT));
        Assert.assertTrue(serviceResponse.getMessage().equalsIgnoreCase("An invalid schedule was given"));
    }
}

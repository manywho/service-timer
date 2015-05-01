package com.manywho.services.timer.service.controllers;

import com.manywho.sdk.entities.run.EngineValue;
import com.manywho.sdk.entities.run.EngineValueCollection;
import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.ServiceBinder;
import com.manywho.sdk.services.listeners.ReflectionListener;
import com.manywho.sdk.services.providers.ObjectMapperProvider;
import com.manywho.services.timer.service.ApplicationBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class WaitControllerTest extends JerseyTest {

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
        String randomToken = UUID.randomUUID().toString();

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.DateTime, LocalDateTime.now().plusSeconds(5).format(DateTimeFormatter.ofPattern("MM/dd/yyyy H:m:s a"))));
            }});
            setToken(randomToken);
        }};

        ServiceResponse response = target("wait/absolute")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(serviceRequest), ServiceResponse.class);

        Assert.assertEquals(InvokeType.Wait, response.getInvokeType());
        Assert.assertEquals(randomToken, response.getToken());
    }

    @Test
    public void testAbsoluteWithInvalidSchedule() throws Exception {
        String randomToken = UUID.randomUUID().toString();

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.DateTime, "THIS ISN'T A DATE, WUT"));
            }});
            setToken(randomToken);
        }};

        Response response = target("wait/absolute")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPost(Entity.json(serviceRequest))
                .invoke();

        ServiceResponse serviceResponse = response.readEntity(ServiceResponse.class);

        Assert.assertEquals(400, response.getStatus());
        Assert.assertTrue(serviceResponse.getRootFaults().containsKey("schedule"));
    }

    @Test
    public void testRelative() throws Exception {
        String randomToken = UUID.randomUUID().toString();

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.String, "in 5 seconds"));
            }});
            setToken(randomToken);
        }};

        ServiceResponse response = target("wait/relative")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(serviceRequest), ServiceResponse.class);

        Assert.assertEquals(InvokeType.Wait, response.getInvokeType());
        Assert.assertEquals(randomToken, response.getToken());
    }

    @Test
    public void testRelativeWithInvalidSchedule() throws Exception {
        String randomToken = UUID.randomUUID().toString();

        ServiceRequest serviceRequest = new ServiceRequest() {{
            setCallbackUri("https://flow.manywho.com/api/run/1/response");
            setInputs(new EngineValueCollection() {{
                add(new EngineValue("Schedule", ContentType.String, "THIS ISN'T A DATE, WUT"));
            }});
            setToken(randomToken);
        }};

        Response response = target("wait/relative")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPost(Entity.json(serviceRequest))
                .invoke();

        ServiceResponse serviceResponse = response.readEntity(ServiceResponse.class);

        Assert.assertEquals(400, response.getStatus());
        Assert.assertTrue(serviceResponse.getRootFaults().containsKey("schedule"));
    }
}

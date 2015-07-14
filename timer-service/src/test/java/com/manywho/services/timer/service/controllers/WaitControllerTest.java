package com.manywho.services.timer.service.controllers;

import com.manywho.sdk.entities.run.EngineValue;
import com.manywho.sdk.entities.run.EngineValueCollection;
import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.providers.ObjectMapperProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class WaitControllerTest extends JerseyTest {

    private static final String AUTHORZATION_HEADER = "ManyWhoTenantId%3D07f799a4-af7c-449b-ba7c-f1f526f7000a%26ManyWhoUserId%3D23c8f059-a4f4-4327-bdea-26cb647ba029%26ManyWhoToken%3Djd%252BxVhSuvlfvkQOLY8%252BHW%252Fq824ssFl%252FPZilCS7HRNRZhSDLXoS92ik4SMliHeRMfWZ7E%252BzgNTdii9J8fTYyqiDAqxXLlddFS3E62pMXkxhEvhdqU1HBaNT8T1y%252Fzrw5zImCI1fXdWa49IO27CxpxH3vb%252F%252FUM4oLT7BIG3vEW6uxp2Uo9V0jRGuO%252F8e7xWG6chiuHoT6OGukHAhaeHo1%252FMFEyretl52pXPv3N9YWwvAzvsJwYhjijkurB%252Bpp4VETbAEt%252BiOtkqbnLxE1kek0VPEtKe%252FpQ3Au1Sm9u6mGWf8tjAUSblrXKnaAMQnn72cKhQXFYnBlohVo3YxTTl2gGSjblzQA2%252Bh3EoypEDtR0wdVdcBgymjunEYHgORswCI9WmLXoiamWuSvBym0TB3IU8oZc5xy%252BUS9YhmJOtU4b3ScF9yyKH%252FPOwuQnBno1h%252FGy1M5GRqxvocUkhn6aPl3omHCjXi9ZMvd%252BNwb248b6RaKKtNok3S1sHIrG%252B2GfocgDZV3iSstLSFPXHHfASYUn80ziXHAJuY0copaqQbpApb59E4W4bGYdnYv%252FGmbI7LveYcbj8jC3%252FFeNrPy9JOgny7IsfcZgV7cyWhaDyx1%252FdAXcf3YeVqZbynZRO5JozKGf%26DirectoryId%3D%40jonjomckay.com%26DirectoryName%3D%40jonjomckay.com%26Email%3Djonjo%40jonjomckay.com%26IdentityProvider%3D%40manywho.com%26TenantName%3D%40jonjomckay.com%26Token%3DDUMMY%26Username%3Djonjo%40jonjomckay.com%26UserId%3D23c8f059-a4f4-4327-bdea-26cb647ba029%26FirstName%3DJonjo%26LastName%3DMcKay";

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
                .header("Authorization", AUTHORZATION_HEADER)
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
                .header("Authorization", AUTHORZATION_HEADER)
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
                .header("Authorization", AUTHORZATION_HEADER)
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
                .header("Authorization", AUTHORZATION_HEADER)
                .buildPost(Entity.json(serviceRequest))
                .invoke();

        ServiceResponse serviceResponse = response.readEntity(ServiceResponse.class);

        Assert.assertEquals(400, response.getStatus());
        Assert.assertTrue(serviceResponse.getRootFaults().containsKey("error"));
        Assert.assertTrue(serviceResponse.getRootFaults().containsValue("An invalid schedule was given"));
    }
}

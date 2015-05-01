package com.manywho.services.timer.service.controllers;

import com.manywho.sdk.entities.describe.DescribeServiceResponse;
import com.manywho.sdk.entities.translate.Culture;
import com.manywho.sdk.services.describe.DescribeServiceBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/metadata")
@Consumes("application/json")
@Produces("application/json")
public class DescribeController {

    @Path("/")
    @POST
    public DescribeServiceResponse describe() {
        return new DescribeServiceBuilder()
                .setProvidesLogic(true)
                .setCulture(new Culture("EN", "US"))
                .createDescribeService()
                .createResponse();
    }
}

package com.manywho.services.timer.service.controllers;

import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.annotations.AuthorizationRequired;
import com.manywho.sdk.services.controllers.AbstractController;
import com.manywho.services.timer.service.entities.WaitAbsoluteRequest;
import com.manywho.services.timer.service.entities.WaitRelativeRequest;
import com.manywho.services.timer.service.services.SchedulerService;
import org.joda.time.DateTime;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Date;
import java.util.List;

@Path("/wait")
@Consumes("application/json")
@Produces("application/json")
public class WaitController extends AbstractController {

    @Inject
    private SchedulerService schedulerService;

    @AuthorizationRequired
    @Path("/absolute")
    @POST
    public ServiceResponse absolute(ServiceRequest serviceRequest) throws Exception {
        WaitAbsoluteRequest waitAbsoluteRequest = this.parseInputs(serviceRequest, WaitAbsoluteRequest.class);
        return scheduleWait(serviceRequest, waitAbsoluteRequest.getSchedule(), "absolute");
    }

    @AuthorizationRequired
    @Path("/relative")
    @POST
    public ServiceResponse relative(ServiceRequest serviceRequest) throws Exception {
        WaitRelativeRequest waitRelativeRequest = this.parseInputs(serviceRequest, WaitRelativeRequest.class);

        List<Date> dates = new PrettyTimeParser().parse(waitRelativeRequest.getSchedule());

        if (dates.isEmpty()) {
            throw new Exception("An invalid schedule was given");
        }

        return scheduleWait(serviceRequest, new DateTime(dates.get(0)), "relative");
    }

    private ServiceResponse scheduleWait(ServiceRequest serviceRequest, DateTime schedule, String type) throws Exception {
        schedulerService.scheduleWait(
                type,
                schedule,
                getAuthenticatedWho(),
                serviceRequest.getStateId(),
                serviceRequest.getTenantId(),
                serviceRequest.getCallbackUri(),
                serviceRequest.getToken()
        );

        return new ServiceResponse(InvokeType.Wait, serviceRequest.getToken());
    }
}

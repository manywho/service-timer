package com.manywho.services.timer.service.controllers;

import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.annotations.AuthorizationRequired;
import com.manywho.sdk.services.controllers.AbstractController;
import com.manywho.services.timer.service.entities.WaitAbsoluteRequest;
import com.manywho.services.timer.service.entities.WaitRelativeRequest;
import com.manywho.services.timer.service.services.SchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Date;
import java.util.List;

@Path("/wait")
@Consumes("application/json")
@Produces("application/json")
public class WaitController extends AbstractController {
    private static final Logger LOGGER = LogManager.getLogger("com.manywho.services.timer");

    @Inject
    private SchedulerService schedulerService;

    @Path("/absolute")
    @POST
    @AuthorizationRequired
    public ServiceResponse absolute(ServiceRequest serviceRequest) throws Exception {
        WaitAbsoluteRequest waitAbsoluteRequest = this.parseInputs(serviceRequest, WaitAbsoluteRequest.class);

        LOGGER.info("Scheduling a new absolute timer: %s", waitAbsoluteRequest.getSchedule());

        return scheduleWait(serviceRequest, waitAbsoluteRequest.getSchedule());
    }

    @Path("/relative")
    @POST
    @AuthorizationRequired
    public ServiceResponse relative(ServiceRequest serviceRequest) throws Exception {
        WaitRelativeRequest waitRelativeRequest = this.parseInputs(serviceRequest, WaitRelativeRequest.class);

        List<Date> dates = new PrettyTimeParser().parse(waitRelativeRequest.getSchedule());

        if (dates.isEmpty()) {
            throw new BadRequestException("An invalid schedule was given");
        }

        LOGGER.info("Scheduling a new relative timer: %s", waitRelativeRequest.getSchedule());

        return scheduleWait(serviceRequest, dates.get(0));
    }

    private ServiceResponse scheduleWait(ServiceRequest serviceRequest, Date schedule) throws Exception {
        schedulerService.scheduleWait(
                schedule,
                getAuthenticatedWho(),
                serviceRequest.getTenantId(),
                serviceRequest.getCallbackUri(),
                serviceRequest.getToken()
        );

        return new ServiceResponse(InvokeType.Wait, serviceRequest.getToken());
    }
}

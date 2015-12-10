package com.manywho.services.timer.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manywho.sdk.entities.run.ServiceProblem;
import com.manywho.sdk.entities.run.ServiceProblemException;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.services.timer.common.jobs.WaitJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

public class SchedulerService {
    private static final Logger LOGGER = LogManager.getLogger("com.manywho.services.timer");

    @Inject
    private Scheduler scheduler;

    @Inject
    private ObjectMapper objectMapper;

    public void scheduleWait(Date schedule, AuthenticatedWho authenticatedWho, String tenantId, String callbackUri, String token) throws Exception {
        Date now = new Date();
        Long dateDiff = Math.abs((schedule.getTime() - now.getTime()) / 1000);
        if (dateDiff < 120) {
            throw new ServiceProblemException("", 500, "Creating wait intervals that are less than 120 seconds is not supported");
        }

        String serializedAuthenticatedWho = objectMapper.writeValueAsString(authenticatedWho);

        JobDetail jobDetail = JobBuilder.newJob(WaitJob.class)
                .withIdentity(UUID.randomUUID().toString())
                .usingJobData("authenticatedWho", serializedAuthenticatedWho)
                .usingJobData("tenantId", tenantId)
                .usingJobData("callbackUri", callbackUri)
                .usingJobData("token", token)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(UUID.randomUUID().toString())
                .startAt(schedule)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LOGGER.error("An error occurred while scheduling a WAIT job", e);

            throw e;
        }
    }
}

package com.manywho.services.timer.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.services.timer.common.jobs.WaitJob;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

public class SchedulerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    @Inject
    private Scheduler scheduler;

    @Inject
    private ObjectMapper objectMapper;

    public void scheduleWait(DateTime schedule, AuthenticatedWho authenticatedWho, String tenantId, String callbackUri, String token) throws Exception {
        if (Seconds.secondsBetween(DateTime.now(), schedule).isLessThan(Seconds.seconds(10))) {
            schedule = DateTime.now();
            schedule = schedule.plusSeconds(10);
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
                .startAt(schedule.toDate())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LOGGER.error("An error occurred while scheduling a WAIT job", e);

            throw e;
        }
    }
}

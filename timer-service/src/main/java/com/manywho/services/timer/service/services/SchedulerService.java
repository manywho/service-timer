package com.manywho.services.timer.service.services;

import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.utils.AuthorizationUtils;
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

    public void scheduleWait(Date schedule, AuthenticatedWho authenticatedWho, String tenantId, String callbackUri, String token) throws Exception {
        String serializedAuthenticatedWho = AuthorizationUtils.serialize(authenticatedWho);

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
            LOGGER.info("Sending the job %s with trigger %s to the scheduler", jobDetail.getKey(), trigger.getKey());

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LOGGER.error(String.format("An error occurred while sending the job %s with trigger %s to the scheduler", jobDetail.getKey(), trigger.getKey()), e);

            throw e;
        }
    }
}

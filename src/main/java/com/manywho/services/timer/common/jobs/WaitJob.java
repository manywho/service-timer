package com.manywho.services.timer.common.jobs;

import com.manywho.sdk.RunService;
import com.manywho.sdk.entities.run.EngineValue;
import com.manywho.sdk.entities.run.EngineValueCollection;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.providers.ObjectMapperProvider;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class needs to stay in this package, as Quartz serializes the entire class name into the job...
 */
public class WaitJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String tenantId = context.getMergedJobDataMap().getString("tenantId");
        String callbackUri = context.getMergedJobDataMap().getString("callbackUri");

        LOGGER.info("Firing job with ID: {}", context.getJobDetail().getKey().getName());

        try {
            AuthenticatedWho authenticatedWho = new ObjectMapperProvider().provide().readValue(context.getMergedJobDataMap().getString("authenticatedWho"), AuthenticatedWho.class);

            new RunService().sendResponse(null, authenticatedWho, tenantId, callbackUri, new ServiceResponse() {{
                setInvokeType(InvokeType.Forward);
                setOutputs(new EngineValueCollection() {{
                    add(new EngineValue("Completed?", ContentType.Boolean, true));
                }});
                setTenantId(context.getMergedJobDataMap().getString("tenantId"));
                setToken(context.getMergedJobDataMap().getString("token"));
            }});
        } catch (Exception e) {
            LOGGER.error("An exception occurred", e);
        }
    }
}

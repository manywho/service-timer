package com.manywho.services.timer.service.quartz;

import org.glassfish.hk2.api.Factory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class QuartzSchedulerFactory implements Factory<Scheduler> {
    private final static Logger LOGGER = LoggerFactory.getLogger(QuartzSchedulerFactory.class);

    @Override
    public Scheduler provide() {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadCount", "3");
        properties.setProperty("org.quartz.jobStore.class", "net.joelinn.quartz.jobstore.RedisJobStore");
        properties.setProperty("org.quartz.jobStore.host", System.getenv("REDIS_URL"));
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "10000");

        try {
            return new StdSchedulerFactory(properties).getScheduler();
        } catch (SchedulerException e) {
            LOGGER.error("Unable to create an instance of Scheduler", e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose(Scheduler scheduler) {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOGGER.error("Unable to shutdown an instance of Scheduler", e);

            throw new RuntimeException(e);
        }
    }
}


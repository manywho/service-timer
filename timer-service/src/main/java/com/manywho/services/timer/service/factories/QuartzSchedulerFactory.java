package com.manywho.services.timer.service.factories;

import org.glassfish.hk2.api.Factory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzSchedulerFactory implements Factory<Scheduler> {
    private final static Logger LOGGER = LoggerFactory.getLogger(QuartzSchedulerFactory.class);

    @Override
    public Scheduler provide() {
        try {
            return new StdSchedulerFactory().getScheduler();
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

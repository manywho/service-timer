package com.manywho.services.timer.service.factories;

import org.glassfish.hk2.api.Factory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzSchedulerFactory implements Factory<Scheduler> {
    @Override
    public Scheduler provide() {
        try {
            return new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            // TODO: Log this, don't do nothing!
            return null;
        }
    }

    @Override
    public void dispose(Scheduler scheduler) {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            // TODO: Log this
        }
    }
}

package com.manywho.services.timer.service.controllers;

import com.manywho.services.timer.service.services.SchedulerService;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class ApplicationTestBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bindFactory(new Factory<Scheduler>() {
            @Override
            public Scheduler provide() {
                try {
                    return StdSchedulerFactory.getDefaultScheduler();
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void dispose(Scheduler scheduler) {

            }
        }).to(Scheduler.class);

        bind(SchedulerService.class).to(SchedulerService.class);
    }
}

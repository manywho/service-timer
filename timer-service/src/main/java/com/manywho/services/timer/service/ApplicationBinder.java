package com.manywho.services.timer.service;

import com.manywho.services.timer.service.factories.QuartzSchedulerFactory;
import com.manywho.services.timer.service.services.SchedulerService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.quartz.Scheduler;

import javax.inject.Singleton;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bindFactory(QuartzSchedulerFactory.class).to(Scheduler.class).in(Singleton.class);

        bind(SchedulerService.class).to(SchedulerService.class);
    }
}

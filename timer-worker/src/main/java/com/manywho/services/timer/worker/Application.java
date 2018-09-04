package com.manywho.services.timer.worker;

import com.manywho.services.timer.common.jobs.quartz.QuartzSchedulerFactory;
import org.quartz.SchedulerException;

public class Application {
    public static void main(String[] args) {
        try {
            new QuartzSchedulerFactory().provide().start();
        } catch (SchedulerException exception) {
            exception.printStackTrace();
        }
    }
}

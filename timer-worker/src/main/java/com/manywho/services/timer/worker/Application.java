package com.manywho.services.timer.worker;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class Application {
    public static void main(String[] args) {
        try {
            StdSchedulerFactory.getDefaultScheduler().start();
        } catch (SchedulerException exception) {
            exception.printStackTrace();
        }
    }
}

package com.manywho.services.timer.migrator;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws SchedulerException {
        Properties oldProperties = new Properties();
        oldProperties.setProperty("org.quartz.threadPool.threadCount", "3");
        oldProperties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        oldProperties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        oldProperties.setProperty("org.quartz.jobStore.dataSource", "service");

        oldProperties.setProperty("org.quartz.dataSource.service.driver", "org.postgresql.Driver");
        oldProperties.setProperty("org.quartz.dataSource.service.maxConnections", "5");
        oldProperties.setProperty("org.quartz.dataSource.service.URL", System.getenv("QUARTZ_DATABASE_URL"));
        oldProperties.setProperty("org.quartz.dataSource.service.user", System.getenv("QUARTZ_DATABASE_USERNAME"));
        oldProperties.setProperty("org.quartz.dataSource.service.password", System.getenv("QUARTZ_DATABASE_PASSWORD"));

        StdSchedulerFactory oldSchedulerFactory = new StdSchedulerFactory(oldProperties);
        oldSchedulerFactory.initialize();

        Scheduler oldScheduler = oldSchedulerFactory.getScheduler();

        StdSchedulerFactory newSchedulerFactory = new StdSchedulerFactory();
        newSchedulerFactory.initialize("new.properties");

        Scheduler newScheduler = newSchedulerFactory.getScheduler();

        Set<JobKey> jobKeys = oldScheduler.getJobKeys(GroupMatcher.anyGroup());

        jobKeys.parallelStream()
                .forEach(jobKey -> {
                    JobDetail jobDetail;
                    try {
                        jobDetail = oldScheduler.getJobDetail(jobKey);
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }

                    LOGGER.info("Found job {}", jobDetail.getKey().getName());

                    try {
                        newScheduler.scheduleJob(jobDetail, new HashSet<>(oldScheduler.getTriggersOfJob(jobKey)), true);
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }

                    LOGGER.info("Scheduled job {}", jobDetail.getKey().getName());
                });

        System.exit(0);
    }
}

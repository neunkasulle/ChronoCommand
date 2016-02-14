package com.github.neunkasulle.chronocommand.control;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Dav on 26.01.2016.
 * Name can be changed everytime
 */
public class SchedulerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerHandler.class);

    private static final SchedulerHandler ourInstance = new SchedulerHandler();

    private static Scheduler sched;

    private SchedulerHandler() {

    }

    public static SchedulerHandler getInstance() {
        return ourInstance;
    }



    private static void intantiateSched() throws SchedulerException {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        sched = schedFact.getScheduler();
    }

    /**
     * schedules all jobs with the triggers
     */
    public static void scheduleAll() {
        try {
            intantiateSched();
        } catch (SchedulerException e) {
            LOGGER.error("Failure in instantiating scheduler", e);
        }

        JobDetail weeklyReminderJob = newJob(WeeklyMailJob.class)
                .withIdentity("weeklyReminderJob", "weeklyReminder") //name, group
                .build();

        JobDetail monthlyReminderJob = newJob(MonthlyReminder.class)
                .withIdentity("monthlyReminderJob", "monthlyReminder")
                .build();

        Trigger lastDayOfMonth = newTrigger() //fires at 12pm every last day every month
                .withIdentity("lastDayTrigger", "monthlyreminder")
                .withSchedule(cronSchedule("0 0 12 L * ?"))
                .startNow()
                .build();

        Trigger weeklyTrigger = newTrigger() //every week at 12pm in every month
                .withIdentity("weeklyTrigger", "weeklyReminder")
                .withSchedule(cronSchedule("0 0 12 ? * L"))
                .startNow()
                .build();

        try {
            sched.start();
            // Tell quartz to schedule a job with a trigger
            sched.scheduleJob(monthlyReminderJob, lastDayOfMonth);
            sched.scheduleJob(weeklyReminderJob, weeklyTrigger);
        } catch (SchedulerException e) {
            LOGGER.error("Failure in starting scheduler", e);
        }
    }

    /**
     * finish running jobs and halts the scheduler's firing of triggers
     */
    public void shutdownSched() {
        try {
            sched.shutdown(true);
        } catch (SchedulerException e) {
            LOGGER.error("Failure in shutdown", e);
        }
    }
}

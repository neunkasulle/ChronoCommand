package com.github.neunkasulle.chronocommand.control;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

/**
 * Created by Dav on 26.01.2016.
 * Name can be changed everytime
 */
public class SchedulerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerHandler.class);

    private static Scheduler intantiateSched() throws SchedulerException {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();
        return sched;
    }

    //TODO call this method once on startup
    /**
     * schedules all jobs with the triggers
     * @throws SchedulerException the one and only
     */
    public static void scheduleAll() throws SchedulerException {
        Scheduler sched = intantiateSched();

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

        sched.start();

        // Tell quartz to schedule a job with a trigger
        sched.scheduleJob(monthlyReminderJob, lastDayOfMonth);
        sched.scheduleJob(weeklyReminderJob, weeklyTrigger);
    }

    /**
     * finish running jobs and halts the scheduler's firing of triggers
     * @param sched the scheduler to halt
     * @throws SchedulerException
     */
    private void shutdownSched(Scheduler sched) throws SchedulerException {
        sched.shutdown(true);//TODO call this method on general shutdown?
    }

    public static void main(String[] args) {

        try {
            scheduleAll();
        } catch (SchedulerException e) {
            LOGGER.error("NOT WORKING");
        }
        LOGGER.debug("Here it comes: \n");

    }

}

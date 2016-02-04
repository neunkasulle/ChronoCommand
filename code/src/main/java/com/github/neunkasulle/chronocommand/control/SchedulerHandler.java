package com.github.neunkasulle.chronocommand.control;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

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

        //this defines the job the scheduler shall do
        JobDetail printJob = newJob(SimpleJob.class)
                .withIdentity("simpleJob", "testGroup") //name, group
                .build();

        Trigger intervalTrigger = newTrigger()
                .withIdentity("intervalTrigger", "testGroup")
                .withSchedule(simpleSchedule()
                    .withIntervalInSeconds(3)
                    .withRepeatCount(3))
                .startAt(futureDate(10, IntervalUnit.SECOND))
                .build();

        Trigger lastDayOfMonth = newTrigger() //fires at 12pm every last day every month
                .withIdentity("lastDayTrigger", "messageAllGroup")
                .withSchedule(cronSchedule("0 0 12 L * ?"))
                .startNow()
                .build();

        sched.start();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(printJob, intervalTrigger);
        sched.scheduleJob(printJob, lastDayOfMonth);
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
            System.err.println("NOT WORKING");
        }

        System.out.println("Here it comes: \n");

       //exit programm manually
    }

}

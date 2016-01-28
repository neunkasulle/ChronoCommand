package com.github.neunkasulle.chronocommand.control;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;

/**
 * Created by Dav on 26.01.2016.
 * Name can be changed everytime
 */
public class SchedulerHandler {

    public void scheduleAll() throws SchedulerException {

        //instantiating the scheduler
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();

        //this defines the job the scheduler shall do
        JobDetail jobA = newJob(SimpleJob.class)
                .withIdentity("job1", "group1")
                .build();

        //this is the trigger
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(dateOf(6, 6, 6)) //start today at 06:06:06
                .build();

        sched.start();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(jobA, trigger);

        //do something here?
        sched.shutdown(true);//kill the scheduler after every job is executed
    }
}

package com.github.neunkasulle.chronocommand.control;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;

import java.util.Date;
import java.lang.Thread;

/**
 * Created by Dav on 26.01.2016.
 */
public class Scheduler {
    //instantiating the scheduler
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
    Scheduler sched = schedFact.getScheduler();

    //this defines the job the scheduler shall do
    // define the job and tie it to our HelloJob class
    JobDetail explode = newJob(Bomb.class) //create a new job class
            .withIdentity("job1", "group1")
            .build();

    //this is the trigger
    // compute a time that is on the next round minute
    Date runTime = evenMinuteDate(new Date());
    Trigger trigger = newTrigger()
            .withIdentity("trigger1", "group1")
            .startAt(runTime)
            .build();

    // Tell quartz to schedule the job using our trigger
    sched.scheduleJob(explode, trigger);

    sched.start();

    Thread.sleep(90L * 1000L); //get some time before the job is triggered

    sched.shutdown(true);//kill the scheduler after every job is executed
}

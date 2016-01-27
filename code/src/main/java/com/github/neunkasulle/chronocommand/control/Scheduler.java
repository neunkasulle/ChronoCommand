package com.github.neunkasulle.chronocommand.control;

//import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;

/**
 * Created by Dav on 26.01.2016.
 */
public class Scheduler {
    /*
    //instantiating the scheduler
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
    Scheduler sched = schedFact.getScheduler();

    //this defines the job the scheduler shall do
    JobDetail explode = newJob(Bomb.class)
            .withIdentity("job1", "group1")
            .build();

    //this is the trigger
    Trigger trigger = newTrigger()
            .withIdentity("trigger1", "group1")
            .startAt(dateOf(6, 6, 6)) //start today at 06:06:06
            .build();

    // Tell quartz to schedule the job using our trigger
    sched.scheduleJob(explode, trigger);

    sched.start();

    //do something here?

    sched.shutdown(true);//kill the scheduler after every job is executed
    */
}

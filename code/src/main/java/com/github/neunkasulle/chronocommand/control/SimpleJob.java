package com.github.neunkasulle.chronocommand.control;

import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.quartz.JobExecutionException;
import java.util.Date;

/**
 * Created by Dav on 27.01.2016.
 */
public class SimpleJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Say Hello to the World and display the date/time
        System.out.println("Hello World! - " + new Date());
    }
}

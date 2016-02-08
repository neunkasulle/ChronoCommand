package com.github.neunkasulle.chronocommand.control;

import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Dav on 27.01.2016.
 */
public class SimpleJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            // Say Hello to the World and display the date/time
            LOGGER.debug("TRIGGERED");
    }
}

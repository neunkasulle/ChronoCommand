package com.github.neunkasulle.chronocommand.control;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class WeeklyMailJobTest extends UeberTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testExecute() throws Exception {

        WeeklyMailJob weeklyMailJob = new WeeklyMailJob();

        weeklyMailJob.execute(null);

    }
}
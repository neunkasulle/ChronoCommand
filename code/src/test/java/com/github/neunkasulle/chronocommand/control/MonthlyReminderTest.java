package com.github.neunkasulle.chronocommand.control;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class MonthlyReminderTest extends UeberTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testExecute() throws Exception {
        MonthlyReminder monthlyReminder = new MonthlyReminder();

        monthlyReminder.execute(null);
        assertTrue(true);
    }
}
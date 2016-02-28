package com.github.neunkasulle.chronocommand.control;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class MonthlyReminderTest extends UeberTest {

    @Test
    public void testExecute() throws Exception {
        MonthlyReminder monthlyReminder = new MonthlyReminder();

        monthlyReminder.execute(null);
        assertTrue(true);
    }
}
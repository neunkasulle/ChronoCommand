package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class ReminderControlTest {

    @Test
    public void testGetInstance() throws Exception {

        assertNotNull(ReminderControl.getInstance());
    }

    @Test(expected = NotYetImplementedException.class)
    public void testCheckForWorkTimeRestrictions() throws Exception {
        ReminderControl.getInstance().checkForWorkTimeRestrictions();
    }

    @Test(expected = NotYetImplementedException.class)
    public void testCheckForOverdueTimeSheets() throws Exception {
        ReminderControl.getInstance().checkForOverdueTimeSheets();
    }
}
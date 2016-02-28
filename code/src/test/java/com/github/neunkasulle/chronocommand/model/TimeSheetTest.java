package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Test;

import java.time.Month;

import static org.junit.Assert.*;

/**
 * Created by Janze on 28.02.2016.
 * Test the model class Time Sheet
 */
public class TimeSheetTest extends UeberTest {

    @Test
    public void testGetId() throws Exception {
        assertTrue(TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom")).getId() != null);
    }

    @Test
    public void testGetCurrentMinutesThisMonth() throws Exception {
        TimeSheet timeSheet = new TimeSheet();

        assertTrue(timeSheet.getCurrentMinutesThisMonth() >= 0);
    }

    @Test
    public void testGetMonth() throws Exception {
        assertEquals(Month.JANUARY ,
                TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom")).getMonth());
    }

    @Test
    public void testGetRequiredHoursPerMonth() throws Exception {
        TimeSheet timeSheet = new TimeSheet();

        assertTrue(timeSheet.getRequiredHoursPerMonth() >= 0);
    }

    @Test
    public void testToString() throws Exception {
        TimeSheet timeSheet = new TimeSheet(new User(), Month.AUGUST, 2016);

        assertEquals("AUGUST 2016", timeSheet.toString());
    }

    @Test
    public void testEquals() throws Exception {
        assertNotEquals(TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("matt")),
                TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom")));
    }

    @Test
    public void testHashCode() throws Exception {
        assertTrue(TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom")).hashCode() >= 0);
    }
}
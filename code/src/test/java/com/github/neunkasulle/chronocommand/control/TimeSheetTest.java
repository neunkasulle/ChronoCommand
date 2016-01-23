package com.github.neunkasulle.chronocommand.control;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 21.01.2016.
 */
public class TimeSheetTest {
    TimeSheetControl timeSheetControl;


    @Before
    public void setUp() throws Exception {
        timeSheetControl = TimeSheetControl.getInstance();

    }

    @Ignore
    @Test
    public void testNewTimeRecordBlank() throws Exception {
        assertTrue(timeSheetControl.newTimeRecord());
    }

    @Ignore
    @Test
    public void testNewTimeRecord1() throws Exception {
        assertTrue(timeSheetControl.newTimeRecord("Test", "did stuff"));
    }

    @Ignore
    @Test
    public void testCloseTimeRecordBlankCorrect() throws Exception {
        timeSheetControl.newTimeRecord();
        assertTrue(timeSheetControl.closeTimeRecord("Test", "did stuff"));
    }
    @Test
    public void testCloseTimeRecordBlankInCorrect() throws Exception {
        timeSheetControl.newTimeRecord();
        assertFalse(timeSheetControl.closeTimeRecord());
    }

    @Ignore
    @Test
    public void testCloseTimeRecordCorrect() throws Exception {
        timeSheetControl.newTimeRecord("Test", "did stuff");
        assertTrue(timeSheetControl.closeTimeRecord());
    }
    @Test
    public void testCloseTimeRecordInCorrect() throws Exception {
        timeSheetControl.newTimeRecord("Test", "did stuff");
        assertFalse(timeSheetControl.closeTimeRecord("Test", "did stuff"));
    }

}
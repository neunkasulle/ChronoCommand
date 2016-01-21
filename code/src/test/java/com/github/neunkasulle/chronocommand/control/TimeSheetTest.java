package com.github.neunkasulle.chronocommand.control;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 21.01.2016.
 */
public class TimeSheetTest {
    TimeSheetControl timeSheetControl;


    @Before
    public void setUp() throws Exception {
        timeSheetControl = new TimeSheetControl();

    }

    @Test
    public void testNewTimeRecordBlank() throws Exception {
        assertTrue(timeSheetControl.newTimeRecord());
    }

    @Test
    public void testNewTimeRecord1() throws Exception {
        assertTrue(timeSheetControl.newTimeRecord("Test", "did stuff"));
    }

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
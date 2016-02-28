package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Test;

import java.io.File;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class TimeSheetHandlerTest extends UeberTest {

    @Test
    public void testWarningTemplate() {
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        timeSheetHandler.setWarningEmailTemplate("FUU");

        assertEquals("FUU", timeSheetHandler.getWarningEmailTemplate());
    }

    @Test
    public void testPDF() {
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        File pdf = timeSheetHandler.createPdfFromTimeSheet(TimeSheetDAO.getInstance()
                .getTimeSheet(Month.JANUARY, 2016, UserDAO.getInstance().findUser("tom")));

        assertTrue(pdf.exists());
    }

}
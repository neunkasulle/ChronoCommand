package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.TimeSheet;
import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.util.List;

/**
 * Created by Janze on 25.02.2016.
 */
public class RegulationControlTest extends UeberTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test(expected = ChronoCommandException.class)
    public void testCheckTimeSheets() throws Exception {
        RegulationControl regulationControl = RegulationControl.getInstance();
        List<TimeSheet> timeSheets = TimeSheetControl.getInstance().getTimeSheets(Month.FEBRUARY, 2016);

        regulationControl.checkTimeSheets(timeSheets);
    }
}
package com.github.neunkasulle.chronocommand.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Created by Dav on 21.01.2016.
 */
public class TimeSheetTest {
    TimeSheet timeSheet;
    Proletarier proletTest;
    Supervisor supervisor;
    TimeRecord timeRecord;

    @Before
    public void setUp() throws Exception {
        proletTest = new Proletarier("tester", "tester@chrono.command", "pw", "tester", supervisor, 40);
        timeSheet = new TimeSheet(proletTest, 1, 1, 40);
    }

    @Test
    public void testSetTimeSheetState() {
        assertFalse(timeSheet.setTimeSheetState(TimeSheetState.UNLOCKED));
    }
}

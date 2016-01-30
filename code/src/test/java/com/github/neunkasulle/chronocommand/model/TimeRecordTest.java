package com.github.neunkasulle.chronocommand.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Created by Dav on 22.01.2016.
 */
public class TimeRecordTest {
    TimeRecord timeRecord;

    @Before
    public void setUp() throws Exception {
        //may change null to objects
        /*TimeSheet sheet = new TimeSheet(null, 1, 1, 40);
        timeRecord = new TimeRecord(sheet, 1, 2, null, "testSheet");*/
    }

    @Test
    public void testCheckTimes() {
        assertFalse(timeRecord.checkTimes());
    }
}

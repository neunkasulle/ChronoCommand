package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class TimeRecordTest extends UeberTest {

    @Test
    public void testCategory() {
       TimeRecord timeRecord = new TimeRecord();
       timeRecord.setCategory(new Category("FUU"));

       assertEquals("FUU", timeRecord.getCategory().getName());
   }

    @Test
    public void testTotalMinutes() {
        TimeRecord timeRecord = new TimeRecord();
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.of(01, 20));
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.of(01, 40));

        timeRecord.setBeginning(begin);
        timeRecord.setEnding(end);

        assertEquals(20, timeRecord.getTotalMinutes());
    }

}
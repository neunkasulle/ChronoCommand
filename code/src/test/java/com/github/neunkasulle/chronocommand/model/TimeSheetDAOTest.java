package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jannis on 02.02.16.
 */
public class TimeSheetDAOTest extends UeberTest {

    @Before
    public void setUp() throws Exception {
        DAOHelper.getInstance().startup("hibernate-inmemory.cfg.xml");
    }

    @After
    public void tearDown() throws Exception {
        DAOHelper.getInstance().shutdown();
    }

    @Test
    public void TestGetTimeRecord() throws Exception {
        Role role = new Role("r");
        User user = new User(role, "a", "b", "d@e.f", "g", null, 5);
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 1984);
        Category category = new Category("c");
        TimeRecord timeRecord = new TimeRecord(LocalDateTime.MIN, LocalDateTime.MAX, category, "bla", timeSheet);

        UserDAO.getInstance().saveRole(role);
        UserDAO.getInstance().saveUser(user);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);
        CategoryDAO.getInstance().saveCategory(category);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);

        TimeRecord dbTimeRecord = TimeSheetDAO.getInstance().getLatestTimeRecord(user);

        assertEquals(timeRecord.getId(), dbTimeRecord.getId());
        assertEquals(timeRecord.getCategory().getId(), dbTimeRecord.getCategory().getId());
        assertEquals("c", dbTimeRecord.getCategory().getName());
    }

    @Test
    public void TestGetTimeRecords() throws Exception {
        Role role = new Role("r");
        User user = new User(role, "a", "b", "d@e.f", "g", null, 5);
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 1984);
        Category category = new Category("c");
        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.MIN, LocalDateTime.MAX, category, "bla", timeSheet);
        TimeRecord timeRecord2 = new TimeRecord(LocalDateTime.MIN, LocalDateTime.MAX, category, "bla", timeSheet);

        UserDAO.getInstance().saveRole(role);
        UserDAO.getInstance().saveUser(user);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);
        CategoryDAO.getInstance().saveCategory(category);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord2);

        List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

        for (TimeRecord dbTimeRecord : timeRecords) {
            assertEquals(timeRecord1.getCategory().getId(), dbTimeRecord.getCategory().getId());
            assertEquals("c", dbTimeRecord.getCategory().getName());
        }
    }
}
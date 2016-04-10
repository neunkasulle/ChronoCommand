package com.github.neunkasulle.chronocommand.model;

import org.hibernate.internal.util.collections.IdentitySet;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by jannis on 29.02.16.
 */
public class GermanLawRegulationsTest {
    Regulations regulations = new GermanLawRegulations();
    User user;
    Category category;
    Role longhours;

    @Before
    public void setUp() throws Exception {
        DAOHelper.getInstance().startup("hibernate-inmemory.cfg.xml");

        Role proletarier = new Role("PROLETARIER", "HIWI", true);
        Set<String> permissions = new IdentitySet(1);
        permissions.add(Role.PERM_PROLETARIER);
        proletarier.setPermissions(permissions);
        UserDAO.getInstance().saveRole(proletarier);
        user = new User(proletarier, "tom", "tom@chronocommand.eu", "cat", "Tom", null, 23);
        UserDAO.getInstance().saveUser(user);

        longhours = new Role("LONGHOURS", "Long hours allowed", false);
        Set<String> longhourPermissions = new IdentitySet(1);
        longhourPermissions.add(Role.PERM_LONGHOURS);
        longhours.setPermissions(longhourPermissions);
        UserDAO.getInstance().saveRole(longhours);

        category = new Category("Programming");
        CategoryDAO.getInstance().saveCategory(category);
    }

    @Test
    public void testCorrect() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 7, 0), LocalDateTime.of(2015, 4, 3, 12, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        TimeRecord timeRecord2 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 13, 0), LocalDateTime.of(2015, 4, 3, 16, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord2);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCorrectWithLongHours() throws Exception {
        user.addRole(longhours);
        UserDAO.getInstance().saveUser(user);

        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 7, 0), LocalDateTime.of(2015, 4, 3, 12, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        TimeRecord timeRecord2 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 13, 0), LocalDateTime.of(2015, 4, 3, 18, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord2);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testWorkedIntoNight() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 21, 31), LocalDateTime.of(2015, 4, 4, 2, 31), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.NIGHTWORK, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkStartedAtNight() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 3, 0), LocalDateTime.of(2015, 4, 3, 8, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.NIGHTWORK, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkFromNightToNight() throws Exception {
        user.setHoursPerMonth(24);
        UserDAO.getInstance().saveUser(user);

        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 10, 2, 0), LocalDateTime.of(2015, 4, 11, 2, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.NIGHTWORK, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkTooLongAtATime() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 7, 0), LocalDateTime.of(2015, 4, 3, 13, 15), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.BREAK, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkTooLong() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 7, 0), LocalDateTime.of(2015, 4, 3, 12, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        TimeRecord timeRecord2 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 13, 0), LocalDateTime.of(2015, 4, 3, 16, 15), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord2);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.MAXIMUM_WORKTIME, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkTooLongWithLongHours() throws Exception {
        user.addRole(longhours);
        UserDAO.getInstance().saveUser(user);

        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord1 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 7, 0), LocalDateTime.of(2015, 4, 3, 12, 0), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord1);

        TimeRecord timeRecord2 = new TimeRecord(LocalDateTime.of(2015, 4, 3, 13, 0), LocalDateTime.of(2015, 4, 3, 18, 15), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord2);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.MAXIMUM_WORKTIME, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkSunday() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.APRIL, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord = new TimeRecord(LocalDateTime.of(2015, 4, 5, 7, 0), LocalDateTime.of(2015, 4, 5, 8, 30), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.SUNDAY_WORK, result.get(0).getRejectionReason());
    }

    @Test
    public void testWorkHoliday() throws Exception {
        TimeSheet timeSheet = new TimeSheet(user, Month.JANUARY, 2015);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);

        TimeRecord timeRecord = new TimeRecord(LocalDateTime.of(2015, 1, 1, 7, 0), LocalDateTime.of(2015, 1, 1, 8, 30), category, "Foo bar", timeSheet);
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);

        List<RegulationRejectionReason> result = regulations.checkTimeSheet(timeSheet);
        assertEquals(RegulationRejectionReason.RejectionReason.HOLIDAY_WORK, result.get(0).getRejectionReason());
    }
}
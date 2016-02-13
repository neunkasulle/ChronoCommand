package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * Created by Janze on 01.02.2016.
 *
 */
public class TimeSheetControlTest extends UeberTest{

    @After
    public void tearDown() throws Exception {

    }

    @Test(expected = ChronoCommandException.class)
    public void testFailingCategory() throws Exception {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        try {
            timeSheetControl.addTimeToSheet(LocalDateTime.now(), LocalDateTime.now(), new Category("FUU"), "BAR" , userDAO.findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assert(e.getReason() == Reason.CATEGORYNOTFOUND);
            throw e;
        }

    }

    @Test
    public void testRightTimeRecordAdd() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        Category category = CategoryDAO.getInstance().findCategoryByString("Programming");
        try {
            timeSheetControl.newTimeRecord(category, "testing", userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(userDAO.findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assert false;
        }
        try {
            timeSheetControl.newTimeRecord(userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(category, "testing", userDAO.findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assert false;
        }
        assert true;

    }

    @Test
    public void testFalseTimeRecordAdd() throws Exception {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        try {
            timeSheetControl.newTimeRecord(userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(userDAO.findUser("tom"));
            assert false;
        }
        catch (ChronoCommandException exc) {
            assert true;
        }
    }

    @Test
    public void editTimeRecordTest() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        fail();
    }

    @Test
    public void closeTimeRecordTest() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        try {
            timeSheetControl.newTimeRecord(null, " ", userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(null, " ", userDAO.findUser("tom"));
        }
        catch (ChronoCommandException ex) {
            assertTrue(ex.getReason() == Reason.MISSINGCATEGORY);
        }
    }

    @Test
    public void getLatestRecordTest(){
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        TimeRecord timeRecord = null;
        try {
            timeSheetControl.newTimeRecord(null, " ", userDAO.findUser("tom"));
            timeRecord = timeSheetControl.getLatestTimeRecord(userDAO.findUser("tom"));
        }
        catch (ChronoCommandException ex) {
            fail();
        }
        assertTrue(timeRecord.getCategory() == null);
    }

    @Test
    public void lockTimeSheetTest() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        Category category = CategoryDAO.getInstance().findCategoryByString("Programming");

        TimeSheet timeSheet = timeSheetControl.getTimeSheet(Month.JANUARY, 2016).get(0);
        assertNotNull(timeSheet);
        timeSheetControl.lockTimeSheet(timeSheet, userDAO.findUser("tom"));

        try {
            timeSheetControl.addTimeToSheet(LocalDateTime.now(), LocalDateTime.now(), category, " ", timeSheet.getUser());
        }
        catch (ChronoCommandException exc) {
            assertTrue(exc.getReason() == Reason.TIMESHEETLOCKED);
        }
    }

    @Test
    public void printAllTimeSheetsUserTest() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        File pdfSheets = timeSheetControl.printAllTimeSheets(userDAO.findUser("tom"));

        assertTrue(pdfSheets != null);

    }

    @Test
    public void printAllTimeSheetsTimeTest() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        File pdfSheets = timeSheetControl.printAllTimeSheets(Month.JANUARY, 2016);

        assertTrue(pdfSheets != null);
    }

    @Test
    public void printTimeSheet() throws Exception {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        TimeSheet timeSheet = new TimeSheet(userDAO.findUser("tom"), Month.AUGUST, 1993);
        File file = timeSheetControl.printTimeSheet(timeSheet);

        assertTrue(file != null);
    }

    @Ignore
    @Test
    public void emailtest() throws Exception {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        User user = new User(null, "neun", "jan@zenkner.eu", "1234", "Fuu Bar", null, 0);

        timeSheetControl.sendEmail(user, "TROLOLOLOLO");
    }

}
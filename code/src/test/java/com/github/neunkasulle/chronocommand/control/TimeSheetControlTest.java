package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
            assert(e.getReason() == Reason.PROJECTNOTFOUND);
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
            timeSheetControl.closeTimeRecord(null, "" ,userDAO.findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assert false;
        }
        assert true;

    }



    @Test
    public void checkForMissingCategory() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        try {
            timeSheetControl.newTimeRecord(null, "a", userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(null, "a", userDAO.findUser("tom"));
        }
        catch (ChronoCommandException ex) {
            assertTrue(ex.getReason() == Reason.MISSINGPROJECT);
        }
    }

    @Test
    public void checkForMissingDescription() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        try {
            timeSheetControl.newTimeRecord(CategoryDAO.getInstance().findCategoryByString("Programming"), " ", userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(null, " ", userDAO.findUser("tom"));
        }
        catch (ChronoCommandException ex) {
            assertTrue(ex.getReason() == Reason.MISSINGDESCRIPTION);
        }
    }

    @Test
    public void getLatestRecordTest() throws ChronoCommandException {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        TimeRecord timeRecord = null;
        timeSheetControl.newTimeRecord(null, " ", userDAO.findUser("tom"));
        timeRecord = timeSheetControl.getLatestTimeRecord(userDAO.findUser("tom"));

        assertTrue(timeRecord.getCategory() == null);
    }
    @Ignore //schlägt immer nur im januar 2016 fehl.. ansonsten wird ein timerecord zum aktuellen timesheet hinzugefügt
    @Test
    public void lockTimeSheetTest() throws Exception{
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        Category category = CategoryDAO.getInstance().findCategoryByString("Programming");

            TimeSheet timeSheet = timeSheetControl.getTimeSheets(Month.JANUARY, 2016).get(0);
            assertNotNull(timeSheet);
            timeSheetControl.lockTimeSheet(timeSheet);
            timeSheetControl.addTimeToSheet(LocalDateTime.now(), LocalDateTime.now(), category, " ", timeSheet.getUser());
    }

    @Test
    public void printAllTimeSheetsUserTest() throws ChronoCommandException {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        File pdfSheets = null;
        pdfSheets = timeSheetControl.printAllTimeSheets(userDAO.findUser("tom"));
        assertNotNull(pdfSheets);
    }

    @Test
    public void printAllTimeSheetsTimeTest() throws ChronoCommandException{
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

            File pdfSheets = timeSheetControl.printAllTimeSheets(Month.JANUARY, 2016);
            assert(pdfSheets != null);

    }

    @Test
    public void printTimeSheet() throws Exception {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        File file = null;

        TimeSheet timeSheet = new TimeSheet(userDAO.findUser("tom"), Month.AUGUST, 1993);
        file = timeSheetControl.printTimeSheet(timeSheet);

        assertNotNull(file);
    }

    @Ignore
    @Test
    public void emailTest() throws Exception {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        User user = new User(null, "neun", "jan@zenkner.eu", "1234", "Fuu Bar", null, 0);

        timeSheetControl.sendEmail(user, "TROLOLOLOLO");
    }

}
package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

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
        LoginControl.getInstance().login("admin", "admin", false);

        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        File pdfSheets = null;
        pdfSheets = timeSheetControl.printAllTimeSheets(userDAO.findUser("tom"));
        assertNotNull(pdfSheets);
    }

    @Test
    public void printAllTimeSheetsTimeTest() throws ChronoCommandException{
        LoginControl.getInstance().login("admin", "admin", false);

        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        File pdfSheets = timeSheetControl.printAllTimeSheets(Month.JANUARY, 2016);
        assertNotNull(pdfSheets);
    }

    @Test
    public void printTimeSheet() throws Exception {
        LoginControl.getInstance().login("admin", "admin", false);

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

    @Test
    public void testGetAllCat() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

        assertTrue(!timeSheetControl.getAllCategories().isEmpty());
    }

    @Test
    public void testGetTimeSheet() {
        try {
            LoginControl.getInstance().login("admin", "admin", true);
        }
        catch(ChronoCommandException e) {
            fail();
        }
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        List<TimeSheet> timeSheets = new LinkedList<>();


        try {
        timeSheets = timeSheetControl.getTimeSheets(Month.FEBRUARY, 2016);
        }
        catch (ChronoCommandException e) {
            fail();
        }

        assertTrue(timeSheets.size() > 0);
    }

    @Test
    public void testGetTimeSheetUser() {
        try {
            LoginControl.getInstance().login("admin", "admin", true);
        }
        catch(ChronoCommandException e) {
            fail();
        }
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        TimeSheet timeSheet = null;


        try {
            timeSheet = timeSheetControl.getTimeSheet(Month.FEBRUARY, 2016, UserDAO.getInstance().findUser("tom"));
        }
        catch (ChronoCommandException e) {
            fail();
        }

        assertNotNull(timeSheet);
    }

    @Test
    public void testGetTimeSheetsUser() {
        try {
            LoginControl.getInstance().login("admin", "admin", true);
        }
        catch(ChronoCommandException e) {
            fail();
        }
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        List<TimeSheet> timeSheets = new LinkedList<>();


        try {
            timeSheets = timeSheetControl.getTimeSheetsFromUser(UserDAO.getInstance().findUser("tom"));
        }
        catch (ChronoCommandException e) {
            fail();
        }

        assertTrue(timeSheets.size() > 0);
    }

    @Test
    public void testGetTimeSheetsUserNotPermitted() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        List<TimeSheet> timeSheets = new LinkedList<>();
        try {
            LoginControl.getInstance().login("tom", "cat", true);
        }
        catch(ChronoCommandException e) {
            fail();
        }

        try {
            timeSheets = timeSheetControl.getTimeSheetsFromUser(UserDAO.getInstance().findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }

    @Test
    public void testGetTimeSheetUserNotPermitted() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        TimeSheet timeSheet = null;
        try {
            LoginControl.getInstance().login("tom", "cat", true);
        }
        catch(ChronoCommandException e) {
            fail();
        }

        try {
            timeSheet = timeSheetControl.getTimeSheet(Month.FEBRUARY, 2016,UserDAO.getInstance().findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }

    @Test
    public void testTimeRecordEdit() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        TimeRecord timeRecord = new TimeRecord();
        LocalDateTime now = LocalDateTime.now();

        try {
            LoginControl.getInstance().login("tom", "cat", true);
            timeRecord = timeSheetControl.getLatestTimeRecord(UserDAO.getInstance().findUser("tom"));

            timeRecord.setBeginning(now);
            timeRecord.setEnding(now);
            timeSheetControl.editTimeRecord(timeRecord);
        }
        catch(ChronoCommandException e) {
            fail();
        }

        assertEquals(0  ,timeRecord.getTotalHours());
    }

    @Test
    public void testTimeRecordEditFailNotPermitted() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        TimeRecord timeRecord = new TimeRecord();
        LocalDateTime now = LocalDateTime.now();

        try {
            LoginControl.getInstance().login("admin", "admin", true);
            timeRecord = timeSheetControl.getLatestTimeRecord(UserDAO.getInstance().findUser("tom"));
            timeSheetControl.editTimeRecord(timeRecord);
        }
        catch(ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }

    @Test
    public void testTimeRecordEditFailLocked() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        TimeRecord timeRecord = new TimeRecord();
        LocalDateTime now = LocalDateTime.now();

        try {
            LoginControl.getInstance().login("tom", "cat", true);
            timeRecord = timeSheetControl.getLatestTimeRecord(UserDAO.getInstance().findUser("tom"));
            timeRecord.getTimeSheet().setTimeSheetState(TimeSheetState.LOCKED);
            timeSheetControl.editTimeRecord(timeRecord);
        }
        catch(ChronoCommandException e) {
            assertEquals(Reason.TIMESHEETLOCKED, e.getReason());
        }
    }

    @Test
    public void testCreateProject() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

        try {
            LoginControl.getInstance().login("admin", "admin", true);
            timeSheetControl.createProject("fuuBAR");
        }
        catch (ChronoCommandException e) {
            fail();
        }

       assertEquals("fuuBAR",
               timeSheetControl.getAllCategories().get(timeSheetControl.getAllCategories().size() - 1).toString());
    }

    @Test
    public void testCreateProjectFailNotPerm() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

        try {
            LoginControl.getInstance().login("tom", "cat", true);
            timeSheetControl.createProject("fuuBAR");
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }

    }

    @Test
    public void testCreateProjectFailMissingProject() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

        try {
            LoginControl.getInstance().login("admin", "admin", true);
            timeSheetControl.createProject("");
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.MISSINGPROJECT, e.getReason());
        }
    }

    @Test
    public void testMessageToSheet() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

        TimeSheet timeSheet = TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom"));

        timeSheetControl.addMessageToTimeSheet(timeSheet, Message.ERROR_BAR);

        assertEquals(Message.ERROR_BAR, timeSheetControl.getMessagesFromTimeSheet(timeSheet).get(0));
    }

    @Test
    public void testGetTimeRecords() {
        try {
            LoginControl.getInstance().login("admin", "admin", false);
            TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

            TimeSheet timeSheet = TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom"));
            assertTrue(timeSheetControl.getTimeRecords(timeSheet).size() > 0);
        }
        catch (ChronoCommandException e) {
            fail();
        }
    }

    @Test
    public void testGetTimeRecordsFail() {
        try {
            LoginControl.getInstance().login("matt", "matt", false);
            TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();

            TimeSheet timeSheet = TimeSheetDAO.getInstance().getLatestTimeSheet(UserDAO.getInstance().findUser("tom"));
            assertTrue(timeSheetControl.getTimeRecords(timeSheet).size() > 0);
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }


}
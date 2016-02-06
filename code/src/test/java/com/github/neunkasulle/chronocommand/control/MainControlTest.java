package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by Janze on 01.02.2016.
 *
 */
public class MainControlTest {

    @Before
    public void setUp() throws Exception {
        MainControl mainControl = MainControl.getInstance();

        mainControl.startup(false);

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testStartup() throws Exception {

    }

    @Test
    public void checkDB() {
        UserDAO userDAO = UserDAO.getInstance();

        User user = userDAO.findUser("tom");

        assertTrue(user.getEmail().equals("tom@chronocommand.eu"));
    }

    @Test
    public void testLogin() {
        LoginControl loginControl = LoginControl.getInstance();
        try {
            loginControl.login("tom", "cat", true);
        }
        catch (ChronoCommandException e) {
            fail();
        }

        assertTrue(loginControl.isLoggedIn());

    }

    @Test(expected = ChronoCommandException.class)
    public void testFailingLogin() throws Exception {
        LoginControl loginControl = LoginControl.getInstance();
        try {
            loginControl.login("tom", "dog", true);
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.BADCREDENTIALS);
            throw e;
        }
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
             //       LocalDateTime.now(), );
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

}
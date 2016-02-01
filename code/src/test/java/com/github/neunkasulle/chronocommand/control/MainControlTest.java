package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Reason;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
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

        mainControl.startup();

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
    public void testFailingLogin() {
        LoginControl loginControl = LoginControl.getInstance();
        try {
            loginControl.login("tom", "cat", true);
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.BADCREDENTIALS);
        }
    }

    @Test(expected = ChronoCommandException.class)
    public void testFailingCategory() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();

        try {
            timeSheetControl.addTimeToSheet(LocalDateTime.now(), LocalDateTime.now(), "FUU","BAR" , userDAO.findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assert(e.getReason() == Reason.CATEGORYNOTFOUND);
        }

    }

    @Test
    public void testRightTimeRecordAdd() {
        TimeSheetControl timeSheetControl = TimeSheetControl.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        try {
            timeSheetControl.newTimeRecord("Programming", "testing", userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord(userDAO.findUser("tom"));
        }
        catch (ChronoCommandException e) {
            assert false;
        }
        try {
            timeSheetControl.newTimeRecord(userDAO.findUser("tom"));
            timeSheetControl.closeTimeRecord("Programming", "testing", userDAO.findUser("tom"));
             //       LocalDateTime.now(), );
        }
        catch (ChronoCommandException e) {
            assert false;
        }
        assert true;

    }

    @Test(expected = ChronoCommandException.class)
    public void testFalseTimeRecordAdd() {

    }


}
package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Reason;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 06.02.2016.
 */
public class LoginControlTest extends UeberTest {

    @After
    public void tearDown() throws Exception {
        LoginControl.getInstance().logout();
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
            loginControl.login("tom", "cat", false);
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
            loginControl.login("tom", "dog", false);
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.BADCREDENTIALS);
            throw e;
        }
    }

    @Test
    public void getUserTest() {
        LoginControl loginControl = LoginControl.getInstance();
        try {
            loginControl.login("tom", "cat", false);

        }
        catch (ChronoCommandException ex) {
            fail();
        }
        try {
            assertTrue(loginControl.getCurrentUser().getUsername().equals("tom"));
        }
        catch (ChronoCommandException ex) {
            fail();
        }
    }

    @Test
    public void getUserTestFailing()  {
        LoginControl loginControl = LoginControl.getInstance();

        try {
            loginControl.getCurrentUser().getUsername().equals("tom");
        }
        catch (ChronoCommandException ex) {
            assertTrue(ex.getReason() == Reason.NOTLOGGEDIN);
        }
    }
}
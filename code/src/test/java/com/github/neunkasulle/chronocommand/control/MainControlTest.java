package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    }

    @Test(expected = ChronoCommandException.class)
    public void testFailingLogin() {

    }

    @Test(expected = ChronoCommandException.class)
    public void testFailingCategory() {

    }

    @Test
    public void testRightTimeRecordAdd() {

    }

    @Test(expected = ChronoCommandException.class)
    public void testFalseTimeRecordAdd() {

    }


}
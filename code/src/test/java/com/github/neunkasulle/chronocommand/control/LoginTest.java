package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Administrator;
import com.github.neunkasulle.chronocommand.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jan on 21.01.2016.
 * Test for the Login Infrastrukture
 */
public class LoginTest {
    User user;
    LoginControl loginControl;

    @Before
    public void setUp() throws Exception {
        user = new Administrator("root", "bla@example.com", "testtest123");
        loginControl = LoginControl.getInstance();
    }

    @Test
    public void testLoginCorrect() throws Exception {
        assertTrue(loginControl.login("root", loginControl.hash("testtest123")));
    }

    @Test
    public void testLoginInCorrect() throws Exception {
        assertFalse(loginControl.login("root", loginControl.hash("123")));
        assertFalse(loginControl.login("rooot", loginControl.hash("testtest123")));
    }

    @Test
    public void testDeterministicHash() throws  Exception {
        assertTrue(loginControl.hash("1234").equals(loginControl.hash("1234")));
    }

    @Test
    public void testNonTrivialHash() {
        assertFalse(loginControl.hash("1234").equals("1234"));
    }

}
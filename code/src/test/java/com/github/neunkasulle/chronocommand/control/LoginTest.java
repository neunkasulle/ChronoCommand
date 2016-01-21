package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Administrator;
import com.github.neunkasulle.chronocommand.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 21.01.2016.
 */
public class LoginTest {
    User user;
    LoginControl loginControl;

    @Before
    public void setUp() throws Exception {
        user = new Administrator("blabla", "bla@example.com", "testtest123");
        loginControl = new LoginControl();
    }

    @Test
    public void testLoginCorrect() throws Exception {
        assertTrue(loginControl.login("blabla", loginControl.hash("testtest123")));
    }

    @Test
    public void testLoginInCorrect() throws Exception {
        assertFalse(loginControl.login("blabla", loginControl.hash("123")));
    }

    @Test
    public void testDeterministicHash() throws  Exception {
        assertTrue(loginControl.hash("1234") == loginControl.hash("1234"));
    }

    @Test
    public void testNonTrivialHash() {
        assertFalse(loginControl.hash("1234") == "1234");
    }

}

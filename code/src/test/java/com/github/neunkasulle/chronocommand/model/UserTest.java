package com.github.neunkasulle.chronocommand.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    User user;

    @Before
    public void setUp() throws Exception {
        user = new Administrator("blabla", "bla@example.com", "testtest123");
    }

    @Ignore
    @Test
    public void testCheckPasswordCorrect() throws Exception {
        //assertTrue(user.checkPassword("testtest123"));
    }

    @Ignore
    @Test
    public void testCheckPasswordWrong() throws Exception {
        //assertFalse(user.checkPassword("wrongpassword"));
    }
}
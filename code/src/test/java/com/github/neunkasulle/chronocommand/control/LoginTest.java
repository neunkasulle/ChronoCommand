package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import org.junit.Before;
import org.junit.Ignore;
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
        user = new User(new Role("user"), "root", "bla@example.com", "testtest123", null, 0);
        loginControl = LoginControl.getInstance();
    }

    @Ignore
    @Test
    public void testLoginCorrect() throws Exception {
        loginControl.login("root", "testtest123", false);
    }

    @Ignore
    @Test(expected = ChronoCommandException.class)
    public void testLoginIncorrectPassword() throws Exception {
        loginControl.login("root", "123", false);
    }

    @Ignore
    @Test(expected = ChronoCommandException.class)
    public void testLoginIncorrectUsername() throws Exception {
        loginControl.login("rooot", "testtest123", false);
    }

    @Test
    public void testNonTrivialHash() {
        assertNotEquals("testtest123", user.getPassword());
    }

}

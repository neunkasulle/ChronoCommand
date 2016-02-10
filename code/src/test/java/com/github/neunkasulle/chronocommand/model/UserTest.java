package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 10.02.2016.
 * test sanity createria
 */
public class UserTest extends UeberTest {
    User user;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        user = new User();
    }

    @Ignore
    @Test
    public void testSetUsername() throws Exception {
        try {
            user.setUsername("  ");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDSTRING);
        }

    }

    @Test
    public void testSetRealname() throws Exception {
        try {
            user.setRealname("  ");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDSTRING);
        }

    }

    @Test
    public void testSetEmail() throws Exception {
        try {
            user.setEmail("tom.tom");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDEMAIL);
        }
        try {
            user.setEmail("tom@tom");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDEMAIL);
        }
    }

    @Test
    public void testSetPassword() throws Exception {
        try {
            user.setPassword("");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDSTRING);
        }
    }

}
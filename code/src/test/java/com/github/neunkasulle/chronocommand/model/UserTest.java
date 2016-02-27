package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void testSetUsernameEmpty() throws Exception {
        try {
            user.setUsername("  ");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDSTRING);
        }
    }

    @Test
    public void testSetUsernameSpecialCase() throws Exception {
        try {
            user.setUsername("ÄÖ$$");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDSTRING);
        }
    }

    @Test
    public void testSetUsernameLong() throws Exception {
        try {
            user.setUsername("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.STRINGTOOLONG);
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
    public void testSetRealnameNull() throws Exception {
        try {
            user.setRealname(null);
            assert false;
        }
        catch (ChronoCommandException e) {
            assertTrue(e.getReason() == Reason.INVALIDSTRING);
        }
    }

    @Test
    public void testGetRealname() {

        assertEquals("Tom", UserDAO.getInstance().findUser("tom").getRealname());
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

    @Test
    public void testGetId() {
        assertNotEquals(UserDAO.getInstance().findUser("tom").getId(), UserDAO.getInstance().findUser("admin").getId());
    }

    @Test
    public void testGetEmail() {

        assertEquals("tom@chronocommand.eu", UserDAO.getInstance().findUser("tom").getEmail());
    }

    @Test
    public void testPlainPw() {
        try {
            user.setPassword("123456");
        }
        catch (ChronoCommandException e) {
            fail();
        }

        assertNotEquals("123456", user.getPassword().toString());
    }

    @Test
    public void testPWEmpty() {
        try {
            user.setPassword("");
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.INVALIDSTRING, e.getReason());
        }
    }

    @Test
    public void testPWTrim() {
        try {
            user.setPassword("  1");
        }
        catch (ChronoCommandException e) {
            assertEquals(Reason.INVALIDSTRING, e.getReason());
        }
    }

    @Test
    public void testPermissions() {

        Set<Role> roles = new HashSet<>();

        roles.add(UserDAO.getInstance().getRoleByName("ADMINISTRATOR"));

        user.setRoles(roles);

        assertTrue(user.isPermitted("administrator"));
    }

    @Test
    public void testPermissionsFalse() {

        Set<Role> roles = new HashSet<>();

        roles.add(UserDAO.getInstance().getRoleByName("PROLETARIER"));

        user.setRoles(roles);

        assertFalse(user.isPermitted("administrator"));
    }

    @Test
    public void testMailFlagTrivial() {
        user.setMailFlag(true);

        assertTrue(user.getMailFlag());
    }

    @Test
    public void testDisableTrivial() {
        user.setDisable(true);

        assertTrue(user.isDisabled());
    }

    @Test
    public void testSupervisor() {
        User sup = new User();
        try {
            sup.setUsername("chef");
        }
        catch (ChronoCommandException e) {
            fail();
        }

        user.setSupervisor(sup);

        assertEquals("chef", user.getSupervisor().getUsername());
    }

    @Test
    public void toStringTest() {
        try {
            user.setRealname("fuu");
            user.setEmail("fuu@bar.bar");
        }
        catch (ChronoCommandException e) {
            fail();
        }

        assertTrue(user.toString().contains(user.getRealname()) && user.toString().contains(user.getEmail()));
    }

}
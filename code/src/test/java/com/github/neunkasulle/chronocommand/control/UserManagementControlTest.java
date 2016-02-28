package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Janze on 25.02.2016.
 */
public class UserManagementControlTest extends UeberTest {
    UserManagementControl userManagementControl;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userManagementControl = UserManagementControl.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        LoginControl.getInstance().logout();
    }

    @Test
    public void testEditUserNormal() throws Exception {
        LoginControl.getInstance().login("tom", "cat", false);
        userManagementControl.editUser(UserDAO.getInstance().findUser("tom"), "toom", "Tomm", "toom@chronocommand.eu", "123456",
                null, 20, false);
        assertEquals(UserDAO.getInstance().findUser("toom").getRealname(), "Tomm");
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedName() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("tom"), "", "Tomm", "toom@chronocommand.eu", "123456",
                null, 20, false);

        assert true;
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedEmail() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("tom"), "toom", "Tomm", "tomchronocommand.eu", "123456",
                null, 20, false);

        assert true;
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedHoursMax() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("tom"), "toom", "Tomm", "toom@chronocommand.eu", "123456",
                null, 9001, false);
        assert true;
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedHoursMin() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("tom"), "toom", "Tomm", "toom@chronocommand.eu", "123456",
                null, -1, false);
        assert true;
    }

    @Test
    public void testFindUserNormal() throws Exception {
        LoginControl.getInstance().login("tom", "cat", false);
        User user = userManagementControl.findUser("tom");

        assertEquals(UserDAO.getInstance().findUser("tom"), user);
    }

    @Test(expected = ChronoCommandException.class)
    public void testFindUserFailLogIn() throws Exception {
        User user = userManagementControl.findUser("tom");

        assert true;
    }

    @Test(expected = ChronoCommandException.class)
    public void testFindUserNoUser() throws Exception {
        LoginControl.getInstance().login("Tom", "cat", false);
        User user = userManagementControl.findUser("tom");

        assert true;
    }

    @Test(expected = ChronoCommandException.class)
    public void testFindUserFailPermitted() throws Exception {
        LoginControl.getInstance().login("matt", "matt", false);
        User user = userManagementControl.findUser("to0000om");

        assert true;
    }

    @Test
    public void testGetUsersByRole() throws Exception {

    }

    @Test
    public void testGetUsersBySupervisor() throws Exception {

    }

    @Test
    public void testGetAllUsers() throws Exception {

    }

    @Test
    public void testCreateInitialAdministrator() throws Exception {

    }

    @Test
    public void testGetAllRoles() throws Exception {

    }

    @Test
    public void testGetRoleByName() throws Exception {

    }
}
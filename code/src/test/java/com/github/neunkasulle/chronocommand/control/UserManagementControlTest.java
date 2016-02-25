package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Janze on 25.02.2016.
 */
public class UserManagementControlTest extends UeberTest {
    UserManagementControl userManagementControl;
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        userManagementControl = UserManagementControl.getInstance();
    }

    @Test
    public void testEditUserNormal() throws Exception {
        LoginControl.getInstance().login("Tom", "cat", true);
        userManagementControl.editUser(UserDAO.getInstance().findUser("Tom"), "Toom", "tomm", "toom@chronocommand.eu", "123456",
                null, 20, false);
        assertEquals(UserDAO.getInstance().findUser("Toom").getRealname(), "tomm");
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedName() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("Tom"), "", "tomm", "toom@chronocommand.eu", "123456",
                null, 20, false);

        assert true;
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedEmail() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("Tom"), "Toom", "tomm", "tom@chronocommand.eu", "123456",
                null, 20, false);

        assert true;
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedHoursMax() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("Tom"), "Toom", "tomm", "toom@chronocommand.eu", "123456",
                null, 9001, false);
        assert true;
    }

    @Test (expected = ChronoCommandException.class)
    public void testEditUserFailedHoursMin() throws Exception {
        userManagementControl.editUser(UserDAO.getInstance().findUser("Tom"), "Toom", "tomm", "toom@chronocommand.eu", "123456",
                null, -1, false);
        assert true;
    }

    @Test
    public void testFindUserNormal() throws Exception {
        LoginControl.getInstance().login("Tom", "cat", true);
        User user = userManagementControl.findUser("Tom");

        assertEquals(UserDAO.getInstance().findUser("Tom"), user);
    }

    @Test(expected = ChronoCommandException.class)
    public void testFindUserFailLogIn() throws Exception {
        User user = userManagementControl.findUser("Tom");

        assert true;
    }

    @Test(expected = ChronoCommandException.class)
    public void testFindUserNoUser() throws Exception {
        LoginControl.getInstance().login("Tom", "cat", true);
        User user = userManagementControl.findUser("Tom");

        assert true;
    }

    @Test(expected = ChronoCommandException.class)
    public void testFindUserFailPermitted() throws Exception {
        LoginControl.getInstance().login("matt", "matt", true);
        User user = userManagementControl.findUser("To0000om");

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
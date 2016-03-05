package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        Role role = UserDAO.getInstance().getRoleByName("administrator");
        List<User> admins = UserDAO.getInstance().getUsersByRole(role);

        assertNotNull(admins);
    }

    @Test
    public void testGetUsersByRoleFail() throws Exception {
        try {
            LoginControl.getInstance().login("tom", "cat", false);

            Role role = UserDAO.getInstance().getRoleByName("administrator");
            UserDAO.getInstance().getUsersByRole(role);
        } catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }

    @Test
    public void testGetUsersBySupervisor() throws Exception {
        User supervisor = UserDAO.getInstance().findUser("tom");
        List<User> users = UserDAO.getInstance().getUsersBySupervisor(supervisor);

        assertNotNull(users);
    }

    @Test
    public void testGetUsersBySupervisorFailPermission() throws Exception {
        try {
            LoginControl.getInstance().login("matt", "matt", false);

            User tom = UserDAO.getInstance().findUser("tom");
            UserDAO.getInstance().getUsersBySupervisor(tom);
        } catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }

    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = UserDAO.getInstance().getAllUsers();

        assertNotNull(users);
    }

    @Test
    public void testGetAllUsersFail() throws Exception {
        try {
            LoginControl.getInstance().login("tom", "cat", false);

            UserManagementControl.getInstance().getAllUsers();
        } catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }

    @Ignore("cant change roles of a user, cant test method")
    @Test
    public void testCreateInitialAdministrator() throws Exception {
        User admin = UserDAO.getInstance().findUser("admin");


        UserManagementControl.getInstance().createInitialAdministrator("firstAdmin", "first@admin", "pw", "chief");
    }

    @Test
    public void testCreateInitialAdministratorFail() throws Exception {
        try {
            UserManagementControl.getInstance().createInitialAdministrator("firstAdmin", "first@admin", "pw", "chief");
        } catch (ChronoCommandException e) {
            assertEquals(Reason.NOTPERMITTED, e.getReason());
        }
    }

    @Test
    public void testGetAllRoles() throws Exception {
        List<Role> roles = UserManagementControl.getInstance().getAllRoles();

        assertNotNull(roles);
    }

    @Test
    public void testGetRoleByName() throws Exception {
        List<Role> roles = UserDAO.getInstance().getAllRoles();
        assertNotNull(roles);

        Role roleByName = UserManagementControl.getInstance().getRoleByName(roles.get(0).getName());
        assertEquals(roleByName, roles.get(0));
    }
}
package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class UserDAOTest extends UeberTest {
    UserDAO userDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        userDAO = UserDAO.getInstance();
        super.setUp();
    }

    @Test
    public void testGetUser() {
        assertEquals(userDAO.findUser("tom"), userDAO.getUser(userDAO.findUser("tom").getId()));
    }

    @Test
    public void testGetUserFail() {

        assertNull(userDAO.getUser((long) 9999999));
    }

    @Test
    public void testFindUserFail() {

        assertNull(userDAO.findUser("KIUJLGGHILOUZFGLI"));
    }

    @Test
    public void testGetUserBySupervisor() {

        User supervisor = userDAO.findUser("tom");

        assertTrue(userDAO.getUsersBySupervisor(supervisor).size() > 0);
    }

    @Test
    public void testGetSupervisorFailNoSupervisor() {

        assertTrue(userDAO.getUsersBySupervisor(userDAO.findUser("matt")).isEmpty());
    }

    @Test
    public void testGetSupervisorFailNoUser() {

        assertTrue(userDAO.getUsersBySupervisor(new User()).isEmpty());
    }

    @Test
    public void testGetUserByRole() {

        assertTrue(userDAO.getUsersByRole(userDAO.getRoleByName("Supervisor")).isEmpty());
    }

    @Test
    public void testGetAllRoles() {

        assertEquals(5, userDAO.getAllRoles().size());
    }

}
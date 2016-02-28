package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class RoleTest extends UeberTest {

    @Test
    public void testGetId() {

        assertTrue(UserDAO.getInstance().getRoleByName("SUPERVISOR").getId() >= 0);
    }

    @Test
    public void testSetName() {
        Role role = new Role();

        role.setName("FUU");

        assertEquals("FUU", role.getName());
    }

    @Test
    public void testGetDescription() {
        Role role = new Role();

        role.setDescription("BAR");

        assertEquals("BAR", role.getDescription());
    }

    @Test
    public void testToString() {
        Role role = new Role();

        role.setDescription("BAR");

        assertEquals("BAR", role.toString());
    }
}
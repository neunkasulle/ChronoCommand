package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class CategoryTest extends UeberTest {


    @Test
    public void testSetName() throws Exception {
        Category category = new Category("WUPWUP");

        category.setName("FOO");

        assertEquals("FOO", category.getName());
    }

    @Test
    public void testHashCode() throws Exception {
        CategoryDAO categoryDAO = CategoryDAO.getInstance();

        assertNotEquals(categoryDAO.findCategoryByString("Programming").hashCode(),
                categoryDAO.findCategoryByString("Procrastination").hashCode());
    }

    @Test
    public void testEqualsFail() throws Exception {

        assertFalse(new Category().equals(new User()));
    }
}
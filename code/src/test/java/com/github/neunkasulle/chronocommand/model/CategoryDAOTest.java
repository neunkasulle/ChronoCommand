package com.github.neunkasulle.chronocommand.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryDAOTest {

    @Before
    public void setUp() throws Exception {
        DAOHelper.getInstance().startup("hibernate-inmemory.cfg.xml");
    }

    @After
    public void tearDown() throws Exception {
        DAOHelper.getInstance().shutdown();
    }

    @Test
    public void testGetOneCategory() throws Exception {
        Category c1 = new Category("c1");
        Category c2 = new Category("c2");
        CategoryDAO.getInstance().saveCategory(c1);
        CategoryDAO.getInstance().saveCategory(c2);
        Category retrieved = CategoryDAO.getInstance().findCategoryByString("c2");
        assertEquals(c2.getId(), retrieved.getId());
        assertEquals(c2.getName(), retrieved.getName());
    }

    @Test
    public void testGetAllCategories() throws Exception {
        Category c1 = new Category("c1");
        Category c2 = new Category("c2");
        CategoryDAO.getInstance().saveCategory(c1);
        CategoryDAO.getInstance().saveCategory(c2);
        List<Category> categoryList = CategoryDAO.getInstance().getAllCategories();
        assertEquals(2, categoryList.size());
        assertTrue(categoryList.contains(c1));
        assertTrue(categoryList.contains(c2));
    }
}
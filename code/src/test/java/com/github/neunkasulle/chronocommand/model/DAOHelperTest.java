package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Janze on 28.02.2016.
 */
public class DAOHelperTest extends UeberTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testStartup() throws Exception {

        DAOHelper.getInstance().startup();
        assertTrue(true);
    }
}
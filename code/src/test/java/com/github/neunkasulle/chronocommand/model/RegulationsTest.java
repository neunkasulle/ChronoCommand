package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.UeberTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Janze on 28.02.2016.
 */
public class RegulationsTest extends UeberTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGetInstance() throws Exception {

        Regulations regulations = Regulations.getInstance();

        assertNotNull(regulations);
    }
}
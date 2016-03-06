package com.github.neunkasulle.chronocommand.control;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Janze on 25.02.2016.
 */
public class RegulationControlTest extends UeberTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCheckTimeSheets() throws Exception {
        RegulationControl regulationControl = RegulationControl.getInstance();

        assertNotNull(regulationControl);

    }
}
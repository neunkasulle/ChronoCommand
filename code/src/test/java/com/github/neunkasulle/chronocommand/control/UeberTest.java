package com.github.neunkasulle.chronocommand.control;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by Janze on 06.02.2016.
 */
public class UeberTest {

    @Before
    public void setUp() throws Exception {
        MainControl mainControl = MainControl.getInstance();
        mainControl.startup(false);
    }

    @After
    public void tearDown() throws Exception {

    }
}
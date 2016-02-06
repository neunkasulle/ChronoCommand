package com.github.neunkasulle.chronocommand.control;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by Janze on 06.02.2016.
 */
public class UeberTest {
    public MainControl mainControl;
    @Before
    public void setUp() throws Exception {
        mainControl = MainControl.getInstance();
        mainControl.startup(false);
    }

    @After
    public void tearDown() throws Exception {

    }
}
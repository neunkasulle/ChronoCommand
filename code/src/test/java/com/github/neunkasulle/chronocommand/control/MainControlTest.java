package com.github.neunkasulle.chronocommand.control;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Janze on 07.02.2016.
 */
public class MainControlTest extends UeberTest {

    @Test
    public void testShutdown() throws Exception {
        mainControl.shutdown();
    }
}
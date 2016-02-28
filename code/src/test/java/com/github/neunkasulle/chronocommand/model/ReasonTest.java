package com.github.neunkasulle.chronocommand.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class ReasonTest {

    @Test
    public void testToString() throws Exception {

        assertEquals("Missing description", Reason.MISSINGDESCRIPTION.toString());
    }
}
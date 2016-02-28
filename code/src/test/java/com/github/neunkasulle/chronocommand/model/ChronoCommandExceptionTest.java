package com.github.neunkasulle.chronocommand.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Janze on 28.02.2016.
 *
 */
public class ChronoCommandExceptionTest {

    @Test
    public void testDetails() {
        ChronoCommandException chronoCommandException = new ChronoCommandException(Reason.NOTPERMITTED, "DOO");

        assertEquals("DOO", chronoCommandException.getDetails());
    }


}
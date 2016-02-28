package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Janze on 28.02.2016.
 */
public class StatisticControlTest {

    @Test
    public void testGetInstance() throws Exception {

        assertNotNull(StatisticControl.getInstance());
    }

    @Test(expected = NotYetImplementedException.class)
    public void testGatherData() throws Exception {
        StatisticControl.getInstance().gatherData();
    }

    @Test(expected = NotYetImplementedException.class)
    public void testFilterData() throws Exception {
        StatisticControl.getInstance().filterData();
    }
}
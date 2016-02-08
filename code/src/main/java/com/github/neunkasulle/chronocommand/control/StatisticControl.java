package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Does statistic stuff
 */
public class StatisticControl {

    private static StatisticControl ourInstance = new StatisticControl();

    private StatisticControl() {
    }

    public static StatisticControl getInstance() {
        return ourInstance;
    }



    public void gatherData() {
        throw new NotYetImplementedException();
    }

    public void filterData() {
        throw new NotYetImplementedException();
    }
}

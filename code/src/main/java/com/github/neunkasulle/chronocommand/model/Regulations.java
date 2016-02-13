package com.github.neunkasulle.chronocommand.model;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 16.01.2016.
 *
 */
public abstract class Regulations {

    public String checkTimeSheet(TimeSheet timeSheet) {
        throw new NotYetImplementedException();
    }
}

package com.github.neunkasulle.chronocommand.model;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 16.01.2016.
 *
 */
public abstract class Regulations {
    private static Regulations instance = null;

    public static void startup(Regulations instance) {
        Regulations.instance = instance;
    }

    public static Regulations getInstance() {
        return instance;
    }

    public abstract String checkTimeSheet(TimeSheet timeSheet);
}

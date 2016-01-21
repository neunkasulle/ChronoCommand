package com.github.neunkasulle.chronocommand.control;

/**
 * Created by Janze on 18.01.2016.
 */
public class ReminderControl extends Control {

    private static ReminderControl ourInstance = new ReminderControl();

    public static ReminderControl getInstance() {
        return ourInstance;
    }

    private ReminderControl() {

    }

    public void checkForWorkTimeRestrictions() {

    }

    public void checkForOverdueTimeSheets() {

    }
}

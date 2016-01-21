package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Sends out Reminders when a month is ending
 */
public class ReminderControl extends Control {

    private static ReminderControl ourInstance = new ReminderControl();

    private ReminderControl() {

    }

    public static ReminderControl getInstance() {
        return ourInstance;
    }



    public void checkForWorkTimeRestrictions() {
        throw new NotYetImplementedException();
    }

    public void checkForOverdueTimeSheets() {
        throw new NotYetImplementedException();
    }
}

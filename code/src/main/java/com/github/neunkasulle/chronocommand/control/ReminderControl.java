package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Sends out Reminders when a month is ending
 */
public class ReminderControl {

    private static ReminderControl ourInstance = new ReminderControl();

    private ReminderControl() {

    }

    /**
     * Gets the one Instance of the ReminderControl.
     * @return The one ReminderControl instance.
     */
    public static ReminderControl getInstance() {
        return ourInstance;
    }

    /**
     * Checks if there are violations in working time and sends out reminders
     */
    public void checkForWorkTimeRestrictions() {
        throw new NotYetImplementedException();
    }

    /**
     * checks if timeSheets are overdue and sends out reminders
     */
    public void checkForOverdueTimeSheets() {
        throw new NotYetImplementedException();
    }
}

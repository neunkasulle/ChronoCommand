package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.github.neunkasulle.chronocommand.model.User;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Deque;
import java.util.List;

/**
 * Created by Janze on 17.01.2016.
 * Checks Time Sheets for regulation violations
 */
public class RegulationControl extends Control {
    Deque<User> warnedUsers;

    private static RegulationControl ourInstance = new RegulationControl();

    private RegulationControl() {

    }

    /**
     * Gets the one Instance of the RegulationControl.
     *
     * @return The one RegulationControl instance.
     */
    public static RegulationControl getInstance() {
        return ourInstance;
    }


    /**
     * Checks timeSheets for incossitencies and rule violations
     *
     * @param timesheets TimeSheets which are to be checked
     */
    public void checkTimeSheets(List<TimeSheet> timesheets) {
        throw new NotYetImplementedException();
    }

}

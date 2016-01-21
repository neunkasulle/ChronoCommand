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

    public static RegulationControl getInstance() {
        return ourInstance;
    }



    public void checkTimeSheets(List<TimeSheet> timesheets) {
        throw new NotYetImplementedException();
    }
    private Deque<User> checkHolidays(List<TimeSheet> timesheets) {

        return null;
    }
    private Deque<User> checkRestrictedTimes(List<TimeSheet> timesheets){

        return null;
    }
    private Deque<User> checkTimeRestrictions(List<TimeSheet> timesheets) {

        return null;
    }

    private Deque<User> checkTimeLimits(List<TimeSheet> timeSheets) {

        return null;
    }

    private void generateWarnings(Deque<User> users) {
        throw new NotYetImplementedException();
    }
}

package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.github.neunkasulle.chronocommand.model.User;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Janze on 17.01.2016.
 */
public class RegulationControl extends Control {
    Deque<User> warnedUsers;

    private static RegulationControl ourInstance = new RegulationControl();

    public static RegulationControl getInstance() {
        return ourInstance;
    }

    private RegulationControl() {

    }

    public void checkTimeSheets(LinkedList<TimeSheet> timesheets) {

    }
    private Deque<User> checkHolidays(LinkedList<TimeSheet> timesheets) {

        return null;
    }
    private Deque<User> checkRestrictedTimes(LinkedList<TimeSheet> timesheets){

        return null;
    }
    private Deque<User> checkTimeRestrictions(LinkedList<TimeSheet> timesheets) {

        return null;
    }

    private Deque<User> checkTimeLimits(LinkedList<TimeSheet> timeSheets) {

        return null;
    }

    private void generateWarnings(Deque<User> users) {

    }
}
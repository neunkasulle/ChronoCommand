package com.github.neunkasulle.ChronoCommand.control;

import com.github.neunkasulle.ChronoCommand.model.TimeSheet;
import com.github.neunkasulle.ChronoCommand.model.User;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Janze on 17.01.2016.
 */
public class RegulationControl extends Control {
    Deque<User> warnedUsers;

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

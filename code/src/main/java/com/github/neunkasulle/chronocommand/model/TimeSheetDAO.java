package com.github.neunkasulle.chronocommand.model;

import java.sql.Time;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;

/**
 * Created by jannis on 19.01.16.
 */
public class TimeSheetDAO {
    private static final TimeSheetDAO instance = new TimeSheetDAO();

    private TimeSheetDAO() {}

    public static TimeSheetDAO getInstance() {
        return instance;
    }

    public TimeSheet getTimeSheet(Month month, Year year, User user) {
        TimeSheet timeSheet = null;

        //TODO HIBERNATE QUERY SPEZIFIK TIME SHEET

        return timeSheet;
    }

    public TimeRecord[] getTimeRecords(TimeSheet timeSheet) {
        throw new UnsupportedOperationException();
    }

    public TimeRecord[] getTimeRecordsByDay(TimeSheet timeSheet, int dayOfMonth) {
        throw new UnsupportedOperationException();
    }

    public TimeSheetHandler getTimeSheetHandler() {
        throw new UnsupportedOperationException();
    }

    public TimeSheet[] getAllUnlockedTimeSheets() {
        throw new UnsupportedOperationException();
    }

    public boolean addTimeSheet(TimeSheet timeSheet) {
        throw new UnsupportedOperationException();
    }

    public List<TimeSheet> getAllTimeSheets(Month month, Year year) {
        UserDAO userDAO = UserDAO.getInstance();
        List<User> users = userDAO.getAllUsers();

        List<TimeSheet> timeSheets = null;

        for(User user: users) {

             timeSheets.add(getTimeSheet(month, year, user));
        }
        return timeSheets;
    }

    public List<TimeSheet> getTimeSheetsFromUser(User user) {

        //TODO HIBERNATE: INSERT QUERY HERE

        return null;
    }
}

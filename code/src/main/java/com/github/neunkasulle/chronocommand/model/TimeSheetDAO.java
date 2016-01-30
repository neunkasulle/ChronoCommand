package com.github.neunkasulle.chronocommand.model;

import java.time.Month;
import java.util.LinkedList;
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

    public TimeSheet getTimeSheet(Month month, int year, User user) {
        TimeSheet timeSheet = null;

        //TODO HIBERNATE QUERY SPECIFIC TIME SHEET

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

    public List<TimeSheet> getAllTimeSheets(Month month, int year) {
        UserDAO userDAO = UserDAO.getInstance();
        List<User> users = userDAO.getAllUsers();

        List<TimeSheet> timeSheets = new LinkedList<>();

        for(User user: users) {

             timeSheets.add(getTimeSheet(month, year, user));
        }
        return timeSheets;
    }

    public List<TimeSheet> getTimeSheetsFromUser(User user) {

        //TODO HIBERNATE: INSERT QUERY HERE

        return null;
    }

    public TimeRecord getLatestTimeRecord(User user) {
        TimeRecord timeRecord = new TimeRecord();

        //TODO HIBERNATE: Get latest time record

        return timeRecord;
    }
}

package com.github.neunkasulle.chronocommand.model;

/**
 * Created by jannis on 19.01.16.
 */
public class TimeSheetDAO {
    private static final TimeSheetDAO instance = new TimeSheetDAO();

    private TimeSheetDAO() {}

    public static TimeSheetDAO getInstance() {
        return instance;
    }

    public TimeSheet getTimeSheet(int month, int year, User proletarier) {
        throw new UnsupportedOperationException();
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
}

package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeSheet {
    int id;
    User proletarier;
    TimeSheetState state;
    int month;
    int year;
    int hoursPerMonth;

    public TimeSheet(User proletarier, int month, int year, int hoursPerMonth) {
        this.proletarier = proletarier;
        this.month = month;
        this.year = year;
        this.hoursPerMonth = hoursPerMonth;
        state = TimeSheetState.UNLOCKED;
    }

    public void addTime(TimeRecord timeRecord) {

    }

    public boolean setTimeSheetState(TimeSheetState state) {
        return false;
    }

    public User getProletarier() { return proletarier; }
}

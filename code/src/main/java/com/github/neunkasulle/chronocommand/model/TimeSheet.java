package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeSheet {
    int id;
    Proletarier proletarier;
    TimeSheetState state;
    int month; // 1-12
    int year;
    int requiredHoursPerMonth;
    int currentHours;

    public TimeSheet(Proletarier proletarier, int month, int year, int hoursPerMonth) {
        this.proletarier = proletarier;
        this.month = month;
        this.year = year;
        this.requiredHoursPerMonth = hoursPerMonth;
        state = TimeSheetState.UNLOCKED;
    }

    public void addTime(TimeRecord timeRecord) {

    }

    public void setCurrentHours(int hours) {
        currentHours = hours;
    }

    public boolean setTimeSheetState(TimeSheetState state) {
        return false;
    }

    public Proletarier getProletarier() { return proletarier; }
}

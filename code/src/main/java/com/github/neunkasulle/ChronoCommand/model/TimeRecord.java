package com.github.neunkasulle.ChronoCommand.model;


/**
 * Created by Janze on 16.01.2016.
 */
public class TimeRecord {
    int id;
    TimeSheet timeSheet;
    int beginn;
    int end;
    Category category;
    String describtion;

    public TimeRecord(TimeSheet timeSheet, int beginn, int end, Category category, String describtion) {
        this.timeSheet = timeSheet;
        this.beginn = beginn;
        this.end = end;
        this.category = category;
        this.describtion = describtion;
    }

    public boolean checkTimes() {
        return false;
    }
}
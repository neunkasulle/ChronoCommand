package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeRecord {
    int id;
    TimeSheet timeSheet;
    //int beginningPause;
    // int endPause;
    int beginning;
    int end;
    Category category;
    String description;

    public TimeRecord(TimeSheet timeSheet, int beginn, int end, Category category, String description) {
        this.timeSheet = timeSheet;
        this.beginning = beginn;
        this.end = end;
        this.category = category;
        this.description = description;
    }

    public int getBeginning() {
        return beginning;
    }

    public int getEnd() {
        return end;
    }

    public boolean checkTimes() {
        return false;
    }
}

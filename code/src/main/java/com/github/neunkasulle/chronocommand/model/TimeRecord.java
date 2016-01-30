package com.github.neunkasulle.chronocommand.model;

import java.time.LocalDateTime;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeRecord {
    int id;
    //int beginningPause;
    // int endPause;
    LocalDateTime beginning;
    LocalDateTime end;
    Category category;
    String description;

    public TimeRecord(LocalDateTime beginn, LocalDateTime end, Category category, String description) {
        this.beginning = beginn;
        this.end = end;
        this.category = category;
        this.description = description;
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean checkTimes() {
        return false;
    }
}

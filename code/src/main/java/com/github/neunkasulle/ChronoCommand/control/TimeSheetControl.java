package com.github.neunkasulle.ChronoCommand.control;

import com.github.neunkasulle.ChronoCommand.model.Proletarier;
import com.github.neunkasulle.ChronoCommand.model.TimeSheet;

import java.io.File;
import java.sql.Time;
import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * Created by Janze on 18.01.2016.
 */
public class TimeSheetControl extends Control {

    public boolean newTimeRecord() {

        return false;
    }

    public boolean newTimeRecord(String category, String description) {

        return false;
    }

    public boolean closeTimeRecord() {

        return false;
    }

    public boolean closeTimeRecord(String category, String description) {

        return false;
    }

    public void addTimeToSheet(Time time) {

    }

    public List<TimeSheet> getSupervisedTimeSheets(Month month, Year year) {

        return null;
    }

    public boolean lockTimeSheet(TimeSheet timeSheet) {

        return false;
    }

    public boolean unlockTimeSheet(TimeSheet timeSheet) {

        return false;
    }

    public boolean approveTimeSheet(TimeSheet timeSheet) {

        return false;
    }

    public void filtersChanged() {

    }

    public File printCheckedTimeSheets(Month month, Year year) {

        return null;
    }

    public File printAllTimeSheets(Month month, Year year) {

        return null;
    }

    public File printTimeSheet(TimeSheet timeSheet) {

        return null;
    }

    public File printAllTimeSheets(Proletarier proletarier) {

        return null;
    }

    public TimeSheet getTimeSheet(Month month, Year year) {


        return null;
    }

}
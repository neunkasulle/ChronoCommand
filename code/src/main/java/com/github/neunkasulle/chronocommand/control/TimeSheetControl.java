package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.io.File;
import java.sql.Time;
import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * Created by Janze on 18.01.2016.
 * Handles TimeSheet manipulation
 */
public class TimeSheetControl extends Control {

    private static TimeSheetControl ourInstance = new TimeSheetControl();

    private TimeSheetControl() {

    }

    public static TimeSheetControl getInstance() {
        return ourInstance;
    }



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
        throw new NotYetImplementedException();
    }

    public List<TimeSheet> getSupervisedTimeSheets(Month month, Year year) {
        //not needed for now
        return null;
    }

    public boolean lockTimeSheet(TimeSheet timeSheet) {

        timeSheet.setTimeSheetState(TimeSheetState.LOCKED);


        //TODO perform some checks here and return false if they fail
        return true;
    }

    public boolean unlockTimeSheet(TimeSheet timeSheet) {

        timeSheet.setTimeSheetState(TimeSheetState.UNLOCKED);

        //TODO permission from validated to unlocked
        return true;
    }

    public boolean approveTimeSheet(TimeSheet timeSheet) {

        timeSheet.setTimeSheetState(TimeSheetState.CHECKED);

        //TODO Permissions checks and stuff?
        return true;
    }

    public void filtersChanged() {
        //not needed for now
    }

    public File printCheckedTimeSheets(Month month, Year year) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        List<TimeSheet> unfilteredTimeSheets = timeSheetDAO.getAllTimeSheets(month, year);
        List<TimeSheet> filteredTimeSheets = null;

        for(TimeSheet timeSheet: unfilteredTimeSheets) {
            if(timeSheet.getState() == TimeSheetState.CHECKED) {
                filteredTimeSheets.add(timeSheet);
            }
        }

        File file = timeSheetHandler.createPdfFromAllTimeSheets(filteredTimeSheets);

        return file;
    }

    public File printAllTimeSheets(Month month, Year year) {

        return null;
    }

    public File printTimeSheet(TimeSheet timeSheet) {

        return null;
    }

    public File printAllTimeSheets(User proletarier) {

        return null;
    }

    public TimeSheet getTimeSheet(Month month, Year year) {


        return null;
    }
    private int getCurrentHours(TimeRecord[] timeRecords) {
        int currentHours = 0;
        for ( TimeRecord timeRecord : timeRecords) {
            currentHours += timeRecord.getEnd() - timeRecord.getBeginning();
        }

        return currentHours;

    }

}

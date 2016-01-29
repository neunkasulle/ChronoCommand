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

        return timeSheetHandler.createPdfFromAllTimeSheets(filteredTimeSheets);
    }

    public File printAllTimeSheets(Month month, Year year) throws NullPointerException {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        List<TimeSheet> timeSheets =timeSheetDAO.getAllTimeSheets(month, year);

        return timeSheetHandler.createPdfFromAllTimeSheets(timeSheets);
    }

    public File printTimeSheet(TimeSheet timeSheet) {
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        return timeSheetHandler.createPdfFromTimeSheet(timeSheet);
    }

    public File printAllTimeSheets(User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        List<TimeSheet> timeSheets = timeSheetDAO.getTimeSheetsFromUser(user);

        return timeSheetHandler.createPdfFromAllTimeSheets(timeSheets);

    }

    public List<Category> getAllCategories() {
        return CategoryDAO.getInstance().getAllCategories();
    }

    public List<TimeSheet> getTimeSheet(Month month, Year year) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        return timeSheetDAO.getAllTimeSheets(month, year);
    }

    private int getCurrentHours(TimeRecord[] timeRecords) {
        int currentHours = 0;
        for ( TimeRecord timeRecord : timeRecords) {
            currentHours += timeRecord.getEnd() - timeRecord.getBeginning();
        }

        return currentHours;
    }

}

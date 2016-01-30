package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.io.File;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import java.time.LocalDate;

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



    public boolean newTimeRecord(User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(LocalDate.now().getMonth(), LocalDate.now().getYear(), user);

        if(timeSheet == null){  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, LocalDate.now().getMonth(), LocalDate.now().getYear());
            timeSheetDAO.addTimeSheet(timeSheet);
        }
        timeSheet.addTime(new TimeRecord(LocalDateTime.now(),LocalDateTime.now(), null, null));
        return true;
    }

    public boolean newTimeRecord(String cat, String description, User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(LocalDate.now().getMonth(), LocalDate.now().getYear(), user);

        Category category = CategoryDAO.getInstance().findCategoryByString(cat);

        if(category == null) {
            return false; // Not category found with this string cat
        }

        if(timeSheet == null){  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, LocalDate.now().getMonth(), LocalDate.now().getYear());
            timeSheetDAO.addTimeSheet(timeSheet);
        }

        timeSheet.addTime(new TimeRecord(LocalDateTime.now(), LocalDateTime.now(), category, description));

        return true;
    }

    public boolean closeTimeRecord(User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeRecord timeRecord = timeSheetDAO.getLatestTimeRecord(user);
        if(timeRecord.getCategory() == null) {
            return false;
        }

        timeRecord.setEnd(LocalDateTime.now());

        return true;
    }

    public boolean closeTimeRecord(String cat, String description, User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeRecord timeRecord = timeSheetDAO.getLatestTimeRecord(user);

        if(timeRecord.getCategory() != null) {
            return false;
        }

        Category category = CategoryDAO.getInstance().findCategoryByString(cat);
        if(category == null) {
            return false;
        }

        timeRecord.setCategory(category);
        timeRecord.setDescription(description);
        timeRecord.setEnd(LocalDateTime.now());

        return false;
    }

    public boolean addTimeToSheet(LocalDateTime beginn, LocalDateTime end, String cat, String description, User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(LocalDate.now().getMonth(), LocalDate.now().getYear(), user);
        Category category = CategoryDAO.getInstance().findCategoryByString(cat);
        if(category == null) {
            return false;
        }

        if(timeSheet == null){  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, LocalDate.now().getMonth(), LocalDate.now().getYear());
            timeSheetDAO.addTimeSheet(timeSheet);
        }

        timeSheet.addTime(new TimeRecord(beginn, end, category, description));

        return true;
    }

    public List<TimeSheet> getSupervisedTimeSheets(Month month, int year) {
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

    public File printCheckedTimeSheets(Month month, int year) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        List<TimeSheet> unfilteredTimeSheets = timeSheetDAO.getAllTimeSheets(month, year);
        List<TimeSheet> filteredTimeSheets = new LinkedList<>();

        for(TimeSheet timeSheet: unfilteredTimeSheets) {
            if(timeSheet.getState() == TimeSheetState.CHECKED){

                    filteredTimeSheets.add(timeSheet);
            }
        }

        return timeSheetHandler.createPdfFromAllTimeSheets(filteredTimeSheets);
    }

    public File printAllTimeSheets(Month month, int year) {
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

    public List<TimeSheet> getTimeSheet(Month month, int year) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();


        return timeSheetDAO.getAllTimeSheets(month, year);
    }

    private int getCurrentHours(TimeRecord[] timeRecords) {
        int currentHours = 0;
        for ( TimeRecord timeRecord : timeRecords) {
            //FIXME Maybe with: https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html#until-java.time.temporal.Temporal-java.time.temporal.TemporalUnit-
            //currentHours += timeRecord.getEnd(). - timeRecord.getBeginning();
        }

        return currentHours;
    }

}

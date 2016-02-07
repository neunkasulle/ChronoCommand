package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;


import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
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

    /**
     * Gets the one Instance of the TimeSheetControl.
     * @return The one TimeSheetControl instance.
     */
    public static TimeSheetControl getInstance() {
        return ourInstance;
    }

    /**
     * creates a new TimeRecord. And a new Time Sheet when there is no time sheet this month
     * @param user The user which the time record belongs to
     */
    public TimeRecord newTimeRecord(User user) throws ChronoCommandException {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(LocalDate.now().getMonth(), LocalDate.now().getYear(), user);

        if(timeSheet == null){  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, LocalDate.now().getMonth(), LocalDate.now().getYear());
            timeSheetDAO.saveTimeSheet(timeSheet);
        }

        TimeRecord timeRecord = new TimeRecord(LocalDateTime.now(), null, null, null, timeSheet);
        timeSheetDAO.saveTimeRecord(timeRecord);

        return timeRecord;
    }

    /**
     * creates a new TimeRecord. And a new Time Sheet when there is no time sheet this month
     * @param category the category of the work performed in this time.
     * @param description description of the work performed.
     * @param user The user which the time record belongs to
     * @throws ChronoCommandException When there is something wrong with e.g. the category
     */
    public TimeRecord newTimeRecord(Category category, String description, User user) throws ChronoCommandException {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(LocalDate.now().getMonth(), LocalDate.now().getYear(), user);


        if(timeSheet == null) {  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, LocalDate.now().getMonth(), LocalDate.now().getYear());
            timeSheetDAO.saveTimeSheet(timeSheet);
        }


        TimeRecord timeRecord = new TimeRecord(LocalDateTime.now(), null, category, description, timeSheet);
        timeSheetDAO.saveTimeRecord(timeRecord);

        return timeRecord;
    }

    /**
     * Closes a time record. Cant be invoked if newTimeRecord was called without category and description
     * @param user The user which the time record belongs to
     * @throws ChronoCommandException ChronoCommandException When there is something wrong with e.g. the category
     */
    public TimeRecord closeTimeRecord(User user) throws ChronoCommandException {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeRecord timeRecord = timeSheetDAO.getLatestTimeRecord(user);
        if(timeRecord.getCategory() == null) {
            throw new ChronoCommandException(Reason.MISSINGCATEGORY);
        }

        timeRecord.setEnd(LocalDateTime.now());
        timeSheetDAO.saveTimeRecord(timeRecord);

        return timeRecord;
    }

    /**
     * Closes a time record. Cant be invoked if newTimeRecord was called with category and description
     * @param category the category of the work performed in this time.
     * @param description description of the work performed.
     * @param user The user which the time record belongs to
     * @throws ChronoCommandException When there is something wrong with e.g. the category
     */
    public TimeRecord closeTimeRecord(Category category, String description, User user) throws ChronoCommandException {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeRecord timeRecord = timeSheetDAO.getLatestTimeRecord(user);

        if (category == null && timeRecord.getCategory() == null) {
            throw new ChronoCommandException(Reason.MISSINGCATEGORY);
        }

        timeRecord.setCategory(category);
        timeRecord.setDescription(description);
        timeRecord.setEnd(LocalDateTime.now());
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);

        return timeRecord;
    }

    public TimeRecord getLatestTimeRecord(User user) {
        return TimeSheetDAO.getInstance().getLatestTimeRecord(user);
    }

    /**
     * Adds a whole time record to the timeSheet
     * @param beginn Start of work
     * @param end end of work
     * @param category the category of the work performed in this time.
     * @param description description of the work performed.
     * @param user The user which the time record belongs to
     * @throws ChronoCommandException When there is something wrong with e.g. the category
     */
    public void addTimeToSheet(LocalDateTime beginn, LocalDateTime end, Category category, String description, User user)
    throws  ChronoCommandException {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(beginn.getMonth(), beginn.getYear(), user);
        if(category == null) {
            throw new ChronoCommandException(Reason.MISSINGCATEGORY);
        }

        if (timeSheet == null) {  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, beginn.getMonth(), beginn.getYear());
            timeSheetDAO.saveTimeSheet(timeSheet);
        }

        if (timeSheet.getState() != TimeSheetState.UNLOCKED) {
            throw new ChronoCommandException(Reason.TIMESHEETLOCKED);
        }

        timeSheetDAO.saveTimeRecord(new TimeRecord(beginn, end, category, description, timeSheet));
    }


    /**
     * Collects all time Sheets from user which are supervised by a specific user
     * @param month the month from which the time sheets will be collected
     * @param year the year from which the time sheets will be collected
     * @param user the supervising user
     * @return A list of time sheets
     */
    /*
    public List<TimeSheet> getSupervisedTimeSheets(Month month, int year, User user) {
        //not needed for now
        return null;
    }
*/
    /**
     * A timeSheet will be locked against changes
     * @param timeSheet the timesheet which will be locked
     */
    public void lockTimeSheet(TimeSheet timeSheet, User user) {

        timeSheet.setTimeSheetState(TimeSheetState.LOCKED);

    }

    /**
     * A TimeSheet will be unlocked again and thus can be changed again
     * @param timeSheet the time sheet which will be unlocked
     */
    public void unlockTimeSheet(TimeSheet timeSheet, User user) {

        timeSheet.setTimeSheetState(TimeSheetState.UNLOCKED);

        //TODO permission from validated to unlocked
    }

    /**
     * A time sheet will be marked as checked
     * @param timeSheet the time sheet which will be marked
     */
    public void approveTimeSheet(TimeSheet timeSheet, User user) {

        timeSheet.setTimeSheetState(TimeSheetState.CHECKED);

        //TODO Permissions checks and stuff?
    }

    /**
     *  Prints out all time sheets which are checked
     * @param month the month of the time sheets
     * @param year the year of the time sheets
     * @return a Pdf File which an be printed
     */
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

    /**
     *  Prints out all time sheets
     * @param month the month of the time sheets
     * @param year the year of the time sheets
     * @return a Pdf File which an be printed
     */
    public File printAllTimeSheets(Month month, int year) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        List<TimeSheet> timeSheets = timeSheetDAO.getAllTimeSheets(month, year);

        return timeSheetHandler.createPdfFromAllTimeSheets(timeSheets);
    }

    /**
     * Prints out a specific time sheet
     * @param timeSheet the time sheet which should be printed
     * @return a Pdf File which an be printed
     */
    public File printTimeSheet(TimeSheet timeSheet) {
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        return timeSheetHandler.createPdfFromTimeSheet(timeSheet);
    }

    /**
     * Prints all time sheets from a specific user
     * @param user the user from which the time sheets are printed
     * @return a Pdf File which an be printed
     */
    public File printAllTimeSheets(User user) {
        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        List<TimeSheet> timeSheets = timeSheetDAO.getTimeSheetsFromUser(user);

        return timeSheetHandler.createPdfFromAllTimeSheets(timeSheets);
    }

    /**
     * Gets all categories for a timerecord
     * @return a list of categories
     */
    public List<Category> getAllCategories() {
        return CategoryDAO.getInstance().getAllCategories();
    }

    /**
     * get all time sheets from a time frame
     * @param month the month of the time sheets
     * @param year the year of the time sheets
     * @return A list of time sheet in the specified time frame
     */
    public List<TimeSheet> getTimeSheet(Month month, int year) {
        return TimeSheetDAO.getInstance().getAllTimeSheets(month, year);
    }

    /**
     * Get all time sheets from a user
     * @param user The user owning the time sheets
     * @return List of all time sheets
     */
    public List<TimeSheet> getTimeSheetsFromUser(User user) {
        return TimeSheetDAO.getInstance().getTimeSheetsFromUser(user);
    }

    /**
     * Gets the current sum of working hours
     * @param timeRecords A number of time records to sum up
     * @return the Number of working hours
     */
    private int getCurrentMinutes(TimeRecord[] timeRecords) {
        int currentMinutes = 0;
        for (TimeRecord timeRecord : timeRecords) {
            currentMinutes += ChronoUnit.MINUTES.between(timeRecord.getBeginning(), timeRecord.getEnd());
        }

        return currentMinutes;
    }

    public void editTimeRecord(TimeRecord timeRecord) throws ChronoCommandException {
        if (timeRecord.getTimeSheet().getState() != TimeSheetState.UNLOCKED) {
            throw new ChronoCommandException(Reason.TIMESHEETLOCKED);
        }
        // TODO check for valid data
        if (!LoginControl.getInstance().getCurrentUser().getId().equals(timeRecord.getTimeSheet().getUser().getId())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);
    }

}

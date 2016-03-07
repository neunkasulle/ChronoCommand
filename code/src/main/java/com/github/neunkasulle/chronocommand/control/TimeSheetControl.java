package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

/**
 * Created by Janze on 18.01.2016.
 * Handles TimeSheet manipulation
 */
public class TimeSheetControl {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSheetControl.class);

    private static final String NEWTIMESHEET = "New time sheet created";
    private static final String LOCKED = "Time sheet is locked";
    private static final String MISSCAT  = "Missing category";
    private static final String MISSDESC  = "Missing description";
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
     * @param category the category of the work performed in this time.
     * @param description description of the work performed.
     * @param user The user which the time record belongs to
     * @throws ChronoCommandException When there is something wrong with e.g. the category
     */
    public TimeRecord newTimeRecord(Category category, String description, User user) throws ChronoCommandException {
        if (!user.isPermitted(Role.PERM_PROLETARIER)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(LocalDate.now().getMonth(), LocalDate.now().getYear(), user);

        if(timeSheet == null) {  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, LocalDate.now().getMonth(), LocalDate.now().getYear());
            timeSheetDAO.saveTimeSheet(timeSheet);
            LOGGER.info(NEWTIMESHEET + LocalDate.now().getMonth() + LocalDate.now().getYear() +user.getUsername());
        }

        if (timeSheet.getState() != TimeSheetState.UNLOCKED) {
            LOGGER.error(LOCKED);
            throw new ChronoCommandException(Reason.TIMESHEETLOCKED);
        }

        TimeRecord timeRecord = new TimeRecord(LocalDateTime.now(), null, category, description, timeSheet);
        timeSheetDAO.saveTimeRecord(timeRecord);
        LOGGER.info("new time record started for" + user.getUsername());

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
        if (!user.isPermitted(Role.PERM_PROLETARIER)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeRecord timeRecord = timeSheetDAO.getLatestTimeRecord(user);

        if (timeRecord.getTimeSheet().getState() != TimeSheetState.UNLOCKED) {
            LOGGER.error(LOCKED);
            throw new ChronoCommandException(Reason.TIMESHEETLOCKED);
        }

        if (category == null && timeRecord.getCategory() == null) {
            LOGGER.error(MISSCAT);
            throw new ChronoCommandException(Reason.MISSINGPROJECT);
        }
        if (description.isEmpty() && timeRecord.getDescription().isEmpty()) {
            LOGGER.error(MISSDESC);
            throw new ChronoCommandException(Reason.MISSINGDESCRIPTION);
        }
        timeRecord.setCategory(category);
        timeRecord.setDescription(description);
        timeRecord.setEnding(LocalDateTime.now());
        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);
        updateCurrentMinutesThisMonth(timeRecord.getTimeSheet());
        LOGGER.info("Time record closed for" + user.getUsername());

        return timeRecord;
    }

    public TimeRecord getLatestTimeRecord(User user) throws ChronoCommandException {

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
        if (!user.isPermitted(Role.PERM_PROLETARIER)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        TimeSheetDAO timeSheetDAO = TimeSheetDAO.getInstance();
        TimeSheet timeSheet = timeSheetDAO.getTimeSheet(beginn.getMonth(), beginn.getYear(), user);

        if(category == null) {
            LOGGER.error(MISSCAT);
            throw new ChronoCommandException(Reason.MISSINGPROJECT);
        }
        if (description.isEmpty()) {
            LOGGER.error(MISSDESC);
            throw new ChronoCommandException(Reason.MISSINGDESCRIPTION);
        }

        if (timeSheet == null) {  //No Time sheet yet, we need to build a new one
            timeSheet = new TimeSheet(user, beginn.getMonth(), beginn.getYear());
            timeSheetDAO.saveTimeSheet(timeSheet);
            LOGGER.info(NEWTIMESHEET  + LocalDate.now().getMonth() + LocalDate.now().getYear() +user.getUsername());
        }

        if (timeSheet.getState() != TimeSheetState.UNLOCKED) {
            LOGGER.error(LOCKED);
            throw new ChronoCommandException(Reason.TIMESHEETLOCKED);
        }

        timeSheetDAO.saveTimeRecord(new TimeRecord(beginn, end, category, description, timeSheet));
        updateCurrentMinutesThisMonth(timeSheet);
        LOGGER.info("Time record created for" + user.getUsername());
    }

    /**
     * A timeSheet will be locked against changes
     * @param timeSheet the timesheet which will be locked
     */
    public void lockTimeSheet(TimeSheet timeSheet) throws ChronoCommandException {
        if (!timeSheet.getUser().equals(LoginControl.getInstance().getCurrentUser())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        String result = Regulations.getInstance().checkTimeSheet(timeSheet);
        if (result.isEmpty()) {
            timeSheet.setTimeSheetState(TimeSheetState.LOCKED);
            TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);
            this.sendEmail(timeSheet.getUser().getSupervisor(), "New locked time sheet from" + timeSheet.getUser().getUsername());
            LOGGER.info("Locked:" + timeSheet.getMonth() + timeSheet.getUser().getUsername());
        } else {
            throw new ChronoCommandException(Reason.TIMESHEETINCOMPLETE, result);
        }
    }

    /**
     * A TimeSheet will be unlocked again and thus can be changed again
     * @param timeSheet the time sheet which will be unlocked
     */
    public void unlockTimeSheet(TimeSheet timeSheet) throws ChronoCommandException {
        if(!LoginControl.getInstance().getCurrentUser().equals(timeSheet.getUser().getSupervisor())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        timeSheet.setTimeSheetState(TimeSheetState.UNLOCKED);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);
        LOGGER.info("unlocked: " + timeSheet.getMonth() + " " + timeSheet.getUser().getUsername());
        this.sendEmail(timeSheet.getUser(), "Time sheet has been unlocked again");
    }

    /**
     * A time sheet will be marked as checked
     * @param timeSheet the time sheet which will be marked
     */
    public void approveTimeSheet(TimeSheet timeSheet) throws ChronoCommandException{
        if(SecurityUtils.getSubject().isPermitted(Role.PERM_SUPERVISOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        timeSheet.setTimeSheetState(TimeSheetState.CHECKED);
        this.sendEmail(timeSheet.getUser(), "Your time sheet has been approved");
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);
        LOGGER.info("checked: " + timeSheet.getMonth() + " " + timeSheet.getUser().getUsername());

    }

    /**
     *  Prints out all time sheets
     * @param month the month of the time sheets
     * @param year the year of the time sheets
     * @return a Pdf File which an be printed
     */
    public File printAllTimeSheets(Month month, int year) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

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
    public File printTimeSheet(TimeSheet timeSheet) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)
                && !LoginControl.getInstance().getCurrentUser().equals(timeSheet.getUser())
                && !LoginControl.getInstance().getCurrentUser().equals(timeSheet.getUser().getSupervisor())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        TimeSheetHandler timeSheetHandler = TimeSheetHandler.getInstance();

        return timeSheetHandler.createPdfFromTimeSheet(timeSheet);
    }

    /**
     * Prints all time sheets from a specific user
     * @param user the user from which the time sheets are printed
     * @return a Pdf File which an be printed
     */
    public File printAllTimeSheets(User user) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

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
    public List<TimeSheet> getTimeSheets(Month month, int year) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        return TimeSheetDAO.getInstance().getAllTimeSheets(month, year);
    }

    public TimeSheet getTimeSheet(Month month, int year, User user) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)
                && !LoginControl.getInstance().getCurrentUser().equals(user)
                && !LoginControl.getInstance().getCurrentUser().equals(user.getSupervisor())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        return TimeSheetDAO.getInstance().getTimeSheet(month, year, user);
    }

    /**
     * Get all time sheets from a user
     * @param user The user owning the time sheets
     * @return List of all time sheets
     */
    public List<TimeSheet> getTimeSheetsFromUser(User user) throws ChronoCommandException {
        if (!LoginControl.getInstance().getCurrentUser().equals(user)
                && !SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)
                && !LoginControl.getInstance().getCurrentUser().equals(user.getSupervisor())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        return TimeSheetDAO.getInstance().getTimeSheetsFromUser(user);
    }

    /**
     * Gets the current sum of working hours
     * @param timeRecords A number of time records to sum up
     * @return the Number of working hours
     */
    private int getCurrentMinutes(List<TimeRecord> timeRecords) {
        int currentMinutes = 0;
        for (TimeRecord timeRecord : timeRecords) {
            currentMinutes += ChronoUnit.MINUTES.between(timeRecord.getBeginning(), timeRecord.getEnding());
        }

        return currentMinutes;
    }

    private void updateCurrentMinutesThisMonth(TimeSheet timeSheet) throws ChronoCommandException {
        int currentMinutes = getCurrentMinutes(TimeSheetDAO.getInstance().getTimeRecords(timeSheet));
        timeSheet.setCurrentMinutesThisMonth(currentMinutes);
        TimeSheetDAO.getInstance().saveTimeSheet(timeSheet);
    }


    public void editTimeRecord(TimeRecord timeRecord) throws ChronoCommandException {

        if (!LoginControl.getInstance().getCurrentUser().getId().equals(timeRecord.getTimeSheet().getUser().getId())) {
            LOGGER.error("not permitted to perform action: editTimeRecord caused by"
                    + LoginControl.getInstance().getCurrentUser().getUsername());
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        if (timeRecord.getTimeSheet().getState() != TimeSheetState.UNLOCKED) {
            LOGGER.error(LOCKED);
            throw new ChronoCommandException(Reason.TIMESHEETLOCKED);
        }

        TimeSheetDAO.getInstance().saveTimeRecord(timeRecord);
        updateCurrentMinutesThisMonth(timeRecord.getTimeSheet());
    }

    public void addMessageToTimeSheet(TimeSheet timeSheet, Message message) {
        timeSheet.setMessage(message);
    }

    public List<Message> getMessagesFromTimeSheet(TimeSheet timeSheet) {
        return timeSheet.getMessages();
    }

    public void sendEmail(User recipient, String message) {

        String host = "mail.teco.edu";
        String port = "25";
        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(properties, null);

        try {
            javax.mail.Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("reminder@chronocommand.eu", "ChronoCommand"));
            msg.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(recipient.getEmail(), recipient.getRealname()));
            msg.setSubject("ChronoCommand Reminder");
            msg.setText(message);
            Transport.send(msg);

        } catch (AddressException e) {
            LOGGER.error("Adress Exception", e);
        } catch (MessagingException e) {
            LOGGER.error("Messaging Exception", e);
        } catch (java.io.UnsupportedEncodingException e) {
            LOGGER.error("Unsupported encoding", e);
        }
    }

    public void createProject(String projectName) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        if (projectName.isEmpty()) {
            throw new ChronoCommandException(Reason.MISSINGPROJECT);
        }

        CategoryDAO.getInstance().saveCategory(new Category(projectName));
    }

    public List<TimeRecord> getTimeRecords(TimeSheet timeSheet) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)
                && !LoginControl.getInstance().getCurrentUser().equals(timeSheet.getUser())
                && !LoginControl.getInstance().getCurrentUser().equals(timeSheet.getUser().getSupervisor())) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }

        return TimeSheetDAO.getInstance().getTimeRecords(timeSheet);
    }
}

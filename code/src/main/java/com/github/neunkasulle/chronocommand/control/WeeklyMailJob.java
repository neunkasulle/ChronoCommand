package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.TimeRecord;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dav on 09.02.2016.
 *
 */
public class WeeklyMailJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerHandler.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<User> recipients = null;

        try {
            recipients = checkUserLastRecord();
        } catch (ChronoCommandException e) {
            LOGGER.error(e.getReason().toString(), e);
        }

        String message = "Your last time record was a week ago. Please record your working time.";

        try {
            for (User user : recipients) {
                TimeSheetControl.getInstance().sendEmail(user, message);
            }
        }
        catch (NullPointerException e) {
            LOGGER.error("Nullptr", e);
        }
    }

    private static List<User> checkUserLastRecord()throws ChronoCommandException {
        List<User> allUser = UserDAO.getInstance().getAllUsers();
        List<TimeRecord> allTimeRecords= new LinkedList<>();
        List<User> recipients = new LinkedList<>();
        for (User user : allUser) {
            try {
                TimeRecord addToList = TimeSheetControl.getInstance().getLatestTimeRecord(user);
                if (addToList != null) {
                    allTimeRecords.add(addToList);
                }
            } catch (ChronoCommandException e) {
                LOGGER.error(e.getReason().toString(), e);
            }
        }
        for (TimeRecord record : allTimeRecords) {
            LocalDateTime lastEndTime = record.getEnding();
            if (lastEndTime.getDayOfMonth() < (LocalDateTime.now().getDayOfMonth() - 7)) {
                recipients.add(record.getTimeSheet().getUser());
            }
        }
        return recipients;
    }


}

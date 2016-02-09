package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Dav on 09.02.2016.
 */
public class WeeklyMailJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<User> recipients = checkUserLastRecord();
        String message = "You should work more and harder lazy ass";

        for (User user : recipients) {
            TimeSheetControl.getInstance().sendEmail(user, message);
        }
    }

    private List<User> checkUserLastRecord() {
        List<User> allUser = UserDAO.getInstance().getAllUsers();
        List<TimeRecord> allTimeRecords= new LinkedList<>();
        List<User> recipients = new LinkedList<>();
        for (User user : allUser) {
            allTimeRecords.add(TimeSheetControl.getInstance().getLatestTimeRecord(user));
        }
        for (TimeRecord record : allTimeRecords) {
            LocalDateTime lastEndTime = record.getEnd();
            if (lastEndTime.getDayOfMonth() < (LocalDateTime.now().getDayOfMonth() - 7)) {
                recipients.add(record.getTimeSheet().getUser());
            }
        }
        return recipients;
    }


}

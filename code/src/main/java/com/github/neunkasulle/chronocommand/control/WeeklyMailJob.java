package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.quartz.JobExecutionException;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

/**
 * Created by Dav on 09.02.2016.
 */
public class WeeklyMailJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //TODO check every User & send mails to them
    }

    private void checkUserLastRecord() {
        List<User> allUser = UserDAO.getInstance().getAllUsers();
        Calendar calendar = Calendar.getInstance();
        List<TimeRecord> allTimeRecords= new LinkedList<>();
        for (User user : allUser) {
            allTimeRecords.add(TimeSheetControl.getInstance().getLatestTimeRecord(user));
        }
        for (TimeRecord record : allTimeRecords) {
            LocalDateTime lastEndTime = record.getEnd();
            //if (lastEndTime.)
        }

    }


}

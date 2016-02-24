package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dav on 09.02.2016.
 * Triggered monthly to send out reminders for users
 */
public class MonthlyReminder implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String message = "Please remember to send in your time sheet for this month.";

        List<User> proletarier = UserDAO.getInstance().getUsersByRole(UserDAO.getInstance().getRoleByName(Role.PERM_PROLETARIER));
        for (User user : proletarier) {
            TimeSheetControl.getInstance().sendEmail(user, message);
        }
        List<User> supervisor = UserDAO.getInstance().getUsersByRole(UserDAO.getInstance().getRoleByName(Role.PERM_SUPERVISOR));
        for (User user : supervisor) {
            TimeSheet sheet = TimeSheetDAO.getInstance().getLatestTimeSheet(user);
            LocalDateTime now = LocalDateTime.now();
            if (sheet.getMonth() == now.getMonth()) {
                TimeSheetControl.getInstance().sendEmail(user, message);
            }
        }
    }
}

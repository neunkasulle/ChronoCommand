package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by Dav on 09.02.2016.
 * Triggered monthly to send out reminders for users
 */
public class MonthlyReminder implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String message = "Please remember to send in your time sheet for this month.";

        Role role = UserDAO.getInstance().getRoleByName("PROLETARIER");
        List<User> proletarier = UserDAO.getInstance().getUsersByRole(role);

        for (User user : proletarier) {
            if (user.getHoursPerMonth() > 0) {
                TimeSheetControl.getInstance().sendEmail(user, message);
            }
        }
        List<User> supervisor = UserDAO.getInstance().getUsersByRole(UserDAO.getInstance().getRoleByName("SUPERVISOR"));
        for (User user : supervisor) {
            if (user.getHoursPerMonth() > 0) {
                TimeSheetControl.getInstance().sendEmail(user, message);
            }
        }
    }
}

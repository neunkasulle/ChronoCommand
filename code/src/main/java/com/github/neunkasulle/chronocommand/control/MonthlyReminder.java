package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
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
        List<User> proletarier = UserDAO.getInstance().getUsersByRole(UserDAO.getInstance().getRoleByName(Role.PERM_PROLETARIER));
        for (User user : proletarier) {
            TimeSheetControl.getInstance().sendEmail(user, message);
        }
        List<User> supervisor = UserDAO.getInstance().getUsersByRole(UserDAO.getInstance().getRoleByName(Role.PERM_SUPERVISOR));
        for (User user : supervisor) {
            TimeSheetControl.getInstance().sendEmail(user, message);
        }
    }
}

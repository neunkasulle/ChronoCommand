package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by Dav on 09.02.2016.
 */
public class MonthlyReminder implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String message = "Last day to check out your time sheet";
        List<User> supervisor = UserDAO.getInstance().getUsersByRole(UserDAO.getInstance().getRoleByName("supervisor"));
        for (User user : supervisor) {
            TimeSheetControl.getInstance().sendEmail(user, message);
        }
    }
}

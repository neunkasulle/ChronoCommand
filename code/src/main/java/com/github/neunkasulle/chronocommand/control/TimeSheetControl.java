package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Proletarier;
import com.github.neunkasulle.chronocommand.model.TimeSheet;
import com.github.neunkasulle.chronocommand.model.TimeSheetState;
import com.github.neunkasulle.chronocommand.model.User;

import java.io.File;
import java.sql.Time;
import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * Created by Janze on 18.01.2016.
 */
public class TimeSheetControl extends Control {

    private static TimeSheetControl ourInstance = new TimeSheetControl();

    public static TimeSheetControl getInstance() {
        return ourInstance;
    }

    private TimeSheetControl() {

    }

    public boolean newTimeRecord() {

        return false;
    }

    public boolean newTimeRecord(String category, String description) {

        return false;
    }

    public boolean closeTimeRecord() {

        return false;
    }

    public boolean closeTimeRecord(String category, String description) {

        return false;
    }

    public void addTimeToSheet(Time time) {

    }

    public List<TimeSheet> getSupervisedTimeSheets(Month month, Year year) {

        return null;
    }

    public boolean lockTimeSheet(TimeSheet timeSheet) {

        User user = UserManagementControl.getInstance().getUser(null);
        //TODO HOW THE HELL DO I GET THE SESSION FROM HERE!?

        boolean correct = timeSheet.setTimeSheetState(TimeSheetState.LOCKED); //TODO WHAT THE ...
        //FIXME HOW TO VALIDATE TIMESHEET AT THIS POINT!?


        if(!correct) {
            return false;
        }

        MessageControl.getInstance().sendMessage(null, timeSheet.getProletarier().getSupervisor().toString()
                , "Stundenzettel agegeben"); //TODO STILL SESSION ID!?
            //TODO .toString() is kind of shitty here

        return correct;
    }

    public boolean unlockTimeSheet(TimeSheet timeSheet) {

        return false;
    }

    public boolean approveTimeSheet(TimeSheet timeSheet) {

        return false;
    }

    public void filtersChanged() {

    }

    public File printCheckedTimeSheets(Month month, Year year) {

        return null;
    }

    public File printAllTimeSheets(Month month, Year year) {

        return null;
    }

    public File printTimeSheet(TimeSheet timeSheet) {

        return null;
    }

    public File printAllTimeSheets(Proletarier proletarier) {

        return null;
    }

    public TimeSheet getTimeSheet(Month month, Year year) {


        return null;
    }

}

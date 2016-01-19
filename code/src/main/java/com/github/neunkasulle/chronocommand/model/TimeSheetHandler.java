package com.github.neunkasulle.chronocommand.model;


/**
 * Created by Janze on 16.01.2016.
 */
public class TimeSheetHandler {
    String warningEmailTemplate;

    public boolean setTimeSheetState(TimeSheet timeSheet, TimeSheetState state) {
        return false;
    }

    public void   createPdfFromTimeSheet(TimeSheet timeSheet){ //TODO find suitable pdf gen lib

    }

    public void   createPdfFromAllTimeSheet(TimeSheet timeSheet){

        //TODO Iteration
        createPdfFromTimeSheet(null);
    }



}

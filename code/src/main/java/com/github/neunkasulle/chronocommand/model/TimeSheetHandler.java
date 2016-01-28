package com.github.neunkasulle.chronocommand.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeSheetHandler {
    String warningEmailTemplate;

    private static final TimeSheetHandler instance = new TimeSheetHandler();

    private TimeSheetHandler() {

    }

    public static TimeSheetHandler getInstance() {
        return instance;
    }

    public File createPdfFromTimeSheet(TimeSheet timeSheet) { //TODO fill timesheet with content
        //new document
        PDDocument newTimesheet = new PDDocument();
        //new page
        try {
            PDPage newPage = new PDPage();
            newTimesheet.addPage(newPage);

            //content: userData, timeData
        } finally {
            try {
                newTimesheet.close(); //most important part!
            } catch (IOException e) {
                // TODO handle exception
            }
        }

        return null;
    }

    public File createPdfFromAllTimeSheets(List<TimeSheet> timeSheets){
        File file = null;

        for (TimeSheet timesheet :
            timeSheets) {
                File tmp =  createPdfFromTimeSheet(timesheet);
                //TODO Ammend to file
        }

        return file;
    }



}

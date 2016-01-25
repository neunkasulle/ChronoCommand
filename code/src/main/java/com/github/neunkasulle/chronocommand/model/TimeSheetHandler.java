package com.github.neunkasulle.chronocommand.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeSheetHandler {
    String warningEmailTemplate;

    /*public boolean setTimeSheetState(TimeSheet timeSheet, TimeSheetState state) {
        return false;
    }*/

    public void createPdfFromTimeSheet(TimeSheet timeSheet) { //TODO fill timesheet with content
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
    }

    public void   createPdfFromAllTimeSheet(TimeSheet timeSheet){

        //TODO Iteration
        createPdfFromTimeSheet(null);
    }



}

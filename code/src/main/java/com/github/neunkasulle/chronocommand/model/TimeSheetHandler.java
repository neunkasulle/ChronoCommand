package com.github.neunkasulle.chronocommand.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;


import java.io.File;
import java.io.FileInputStream;
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
        /*
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
                System.err.println("Something went wrong.");
            }
        }
        */
        return null;
    }

    public File createPdfFromTimeSheet() {
        PDDocument pdfTimeSheet = null;
        File file = null;
        PDPage page = null;
        try {
            file = new File("C:\\Users\\Dav\\Documents\\ChronoCommand\\code\\src\\main\\resources\\Stundenzettel.pdf");
            pdfTimeSheet = PDDocument.load(file);
        } catch (Exception e) {

            System.err.println("Loading error.");
        }
        try {
            PDFont font = PDType1Font.HELVETICA;

            page = pdfTimeSheet.getPage(0);
            PDPageContentStream contents = new PDPageContentStream(pdfTimeSheet, page, true, true);
            contents.beginText();
            //fill name etc
            contents.setFont(font, 12);
            contents.newLineAtOffset(300, 710);
            contents.showText("David Kuhmann");//name
            contents.newLineAtOffset(160, 28);
            contents.showText("01");//month
            contents.newLineAtOffset(50, 0);
            contents.showText("2016");//year
            contents.newLineAtOffset(-18, -96);
            contents.showText("9,45");//money
            contents.endText();

            //fill timesheet
            contents.beginText();
            contents.setFont(font, 10);
            contents.newLineAtOffset(55, 570);
            contents.showText("Stundenzettel ausfüllen");//
            contents.newLineAtOffset(150, 0);
            contents.showText("01.02.2016");//date
            contents.newLineAtOffset(90, 0);
            contents.showText("11:30");//start
            contents.newLineAtOffset(70, 0);
            contents.showText("15:30");//end
            contents.newLineAtOffset(75, 0);
            contents.showText("00:00");//pause
            contents.newLineAtOffset(80, 0);
            contents.showText("04:00");//total time
            contents.endText();
            contents.beginText();
            contents.setFont(font, 10);
            contents.newLineAtOffset(55, 555);
            contents.showText("Stundenzettel ausfüllen");//
            contents.newLineAtOffset(150, 0);
            contents.showText("01.02.2016");//date
            contents.newLineAtOffset(90, 0);
            contents.showText("11:30");//start
            contents.newLineAtOffset(70, 0);
            contents.showText("15:30");//end
            contents.newLineAtOffset(75, 0);
            contents.showText("00:00");//pause
            contents.newLineAtOffset(80, 0);
            contents.showText("04:00");//total time
            contents.endText();

            contents.close();
            pdfTimeSheet.save("C:\\Users\\Dav\\Downloads\\Study.pdf");

        } catch (Exception e) {
            System.err.println("Content not loaded.");
        }
        try {
            pdfTimeSheet.close();
        } catch (Exception e) {
            System.err.println("It's fucked up. Nobody can save you.");
        }
        return null;
    }

    public File createPdfFromAllTimeSheets(List<TimeSheet> timeSheets){
        File file = null;

        for (TimeSheet timesheet : timeSheets) {
                File tmp =  createPdfFromTimeSheet(timesheet);
                //TODO Ammend to file
        }

        return file;
    }

    public static void main(String[] args) {
        TimeSheetHandler handler = TimeSheetHandler.getInstance();
        handler.createPdfFromTimeSheet();

        System.exit(0);
    }

}

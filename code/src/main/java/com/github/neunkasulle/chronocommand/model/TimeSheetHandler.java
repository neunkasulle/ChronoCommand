package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.MainControl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.time.Month;
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

    /**
     * creates a printable timesheet pdf
     * @param timeSheet a timesheet
     * @return a pdf
     */
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
            PDFont fontBold = PDType1Font.HELVETICA_BOLD;

            page = pdfTimeSheet.getPage(0);
            PDPageContentStream contents = new PDPageContentStream(pdfTimeSheet, page, true, true);
            contents.beginText();
            //fill name etc
            contents.setFont(font, 12);
            contents.newLineAtOffset(278, 742);
            contents.showText("David Kuhmann");//name
            contents.newLineAtOffset(185, 22);
            contents.showText("01 ");//month
            contents.setFont(fontBold, 12);
            contents.showText("/ ");
            contents.setFont(font, 12);
            contents.showText("2016");//year
            contents.newLineAtOffset(0, -64);
            contents.showText("9,45");//money
            contents.endText();

            //Tätigkeit bulk
            contents.beginText();
            contents.setFont(font, 10);
            contents.newLineAtOffset(60, 637);
            contents.showText("Stundenzettel ausgefüllt");//Tätigkeit
            contents.endText();
            //Kategorie bulk
            contents.beginText();
            contents.newLineAtOffset(197, 637);
            contents.showText("Tutorium");//Kategorie
            contents.endText();
            //Time bulk, may need no extra offset calculation
            contents.beginText();
            contents.newLineAtOffset(300, 637);
            contents.showText("01.02.16 11:30");//start
            contents.newLineAtOffset(82, 0);
            contents.showText("01.02.16 15:30");//end
            contents.newLineAtOffset(100, 0);
            contents.showText("04:00h");//total time
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

    /**
     * creates one pdf with all given timesheets
     * @param timeSheets list of timesheets
     * @return one pdf with all timesheets
     */
    public File createPdfFromAllTimeSheets(List<TimeSheet> timeSheets){
        File file = null;

        for (TimeSheet timesheet : timeSheets) {
                File tmp =  createPdfFromTimeSheet(timesheet);
                //TODO Ammend to file
        }

        return file;
    }

    public static void main(String[] args) {
        MainControl.getInstance().startup();
        TimeSheetHandler handler = TimeSheetHandler.getInstance();
        handler.createPdfFromTimeSheet(TimeSheetDAO.getInstance().getTimeSheet(Month.JANUARY, 2016, UserDAO.getInstance().findUser("tom")));

        System.exit(0);
    }

}

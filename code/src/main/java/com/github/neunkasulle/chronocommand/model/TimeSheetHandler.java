package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.MainControl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

/**
 * Created by Janze on 16.01.2016.
 */
@Entity
public class TimeSheetHandler {
    @Id
    @GeneratedValue
    Long id;

    @Basic(optional = false)
    String warningEmailTemplate;

    private static final TimeSheetHandler instance = new TimeSheetHandler();

    private TimeSheetHandler() {

    }

    public static TimeSheetHandler getInstance() {
        return instance;
    }

    public String getWarningEmailTemplate() {
        return warningEmailTemplate;
    }

    public void setWarningEmailTemplate(String warningEmailTemplate) {
        this.warningEmailTemplate = warningEmailTemplate;
    }

    /**
     * creates a printable timesheet pdf
     * @param timeSheet a timesheet
     * @return a pdf
     */
    public File createPdfFromTimeSheet(TimeSheet timeSheet) { //TODO fill timesheet with content
        List<TimeRecord> recordsToPDF = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

        PDDocument pdfTimeSheet = null;
        File file = null;
        try {
            file = new File("C:\\Users\\Dav\\Documents\\ChronoCommand\\code\\src\\main\\resources\\Stundenzettel.pdf");
            pdfTimeSheet = PDDocument.load(file);
        } catch (Exception e) {
            System.err.println("Loading error.");
        }
        int yOff = 0;
        try {
            PDFont font = PDType1Font.HELVETICA;
            PDFont fontBold = PDType1Font.HELVETICA_BOLD;

            PDPage page = pdfTimeSheet.getPage(0);
            PDPageContentStream contents = new PDPageContentStream(pdfTimeSheet, page, true, true);
            contents.beginText();
            //fill name etc
            contents.setFont(font, 12);
            contents.newLineAtOffset(278, 742);
            contents.showText(timeSheet.getUser().realName);//name
            contents.newLineAtOffset(185, 22);
            contents.showText(timeSheet.month.toString());//month
            contents.setFont(fontBold, 12);
            contents.showText(" / ");
            contents.setFont(font, 12);
            contents.showText(Integer.toString(timeSheet.year));//year
            contents.newLineAtOffset(0, -64);
            contents.showText("9,45");//money
            contents.endText();

            //Tätigkeit bulk
            yOff = 637;
            for (TimeRecord timeR : recordsToPDF) {
                contents.beginText();
                contents.setFont(font, 10);
                contents.newLineAtOffset( 60, yOff);
                contents.showText(timeR.getDescription());
                contents.endText();
                yOff = yOff - 17;
            }

            //Kategorie bulk
            yOff = 637;
            for (TimeRecord timeR : recordsToPDF) {
                contents.beginText();
                contents.newLineAtOffset(197, yOff);
                contents.showText(timeR.getCategory().toString());//Kategorie
                contents.endText();
                yOff = yOff - 17;
            }
            //Time bulk, may need no extra offset calculation
            yOff = 637;
            for (TimeRecord timeR : recordsToPDF) {
                contents.beginText();
                contents.newLineAtOffset(300, yOff);
                contents.showText(timeR.getBeginning().toString());//start
                contents.newLineAtOffset(82, 0);
                contents.showText("");//end
                contents.newLineAtOffset(100, 0);
                contents.showText(timeR.getTotalTime());//total time
                contents.endText();
                yOff -= 17;
            }
            contents.close();
            pdfTimeSheet.save("C:\\Users\\Dav\\Downloads\\Study.pdf");

        } catch (Exception e) {
            System.err.println("problem in content section");
        }
        try {
            pdfTimeSheet.close();
        } catch (Exception e) {
            System.err.println("closing went wrong");
        }
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
        int yOff = 0;
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
            yOff = 637;
            for (int i = 0; i < 5; i++) {
                contents.beginText();
                contents.setFont(font, 10);
                contents.newLineAtOffset( 60, yOff);
                contents.showText("Stundenzettel bearbeitet");
                contents.endText();
                yOff = yOff - 17;
            }

            //Kategorie bulk
            yOff = 637;
            for (int i = 0; i < 5; i++) {
                contents.beginText();
                contents.newLineAtOffset(197, yOff);
                contents.showText("Tutorium");//Kategorie
                contents.endText();
                yOff = yOff - 17;
            }
            //Time bulk, may need no extra offset calculation
            yOff = 637;
            for (int i = 0; i < 3; i++) {
                contents.beginText();
                contents.newLineAtOffset(300, yOff);
                contents.showText("01.02.16 11:30");//start
                contents.newLineAtOffset(82, 0);
                contents.showText("01.02.16 15:30");//end
                contents.newLineAtOffset(100, 0);
                contents.showText("01:30h");//total time
                contents.endText();
                yOff -= 17;
            }
            contents.close();
            pdfTimeSheet.save("C:\\Users\\Dav\\Downloads\\Study.pdf");

        } catch (Exception e) {
            System.err.println("problem in content section");
        }
        try {
            pdfTimeSheet.close();
        } catch (Exception e) {
            System.err.println("close() went wrong");
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
        //handler.createPdfFromTimeSheet();

        System.exit(0);
    }

}

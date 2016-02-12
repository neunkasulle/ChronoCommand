package com.github.neunkasulle.chronocommand.model;

import com.github.neunkasulle.chronocommand.control.MainControl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Janze on 16.01.2016.
 */
@Entity
public class TimeSheetHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSheetHandler.class);

    @Id
    @GeneratedValue
    Long id;

    @Basic(optional = false)
    String warningEmailTemplate; //TODO do we need this?

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
    //TODO @Dav add new page when timesheet is "full"
    public File createPdfFromTimeSheet(TimeSheet timeSheet) {
        PDDocument pdfTimeSheet;
        File file;
        FileOutputStream outputFile;
        try {
            file = new File(getClass().getClassLoader().getResource("Stundenzettel.pdf").getFile());
            outputFile = new FileOutputStream("Study.pdf");//TODO save different
            pdfTimeSheet = PDDocument.load(file);
        } catch (Exception e) {
            LOGGER.error("Loading error.");
            return null;
        }
        try {
            PDPage page = pdfTimeSheet.getPage(0);
            PDPageContentStream contents = new PDPageContentStream(pdfTimeSheet, page, true, true);

            fillContent(contents, timeSheet);

            pdfTimeSheet.save(outputFile);
        } catch (Exception e) {
            LOGGER.error("problem in content section");
        }
        try {
            pdfTimeSheet.close();
        } catch (Exception e) {
            LOGGER.error("closing went wrong");
        }
        File returnFile = new File("Study.pdf");
        return returnFile;
    }

    private void fillContent(PDPageContentStream contents, TimeSheet timeSheet) throws IOException {
        List<TimeRecord> recordsToPDF = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

        int yOff = 0;
        PDFont font = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        //fill Content
        contents.beginText();
        contents.setFont(font, 12);
        //fill name etc
        contents.newLineAtOffset(465, 764);
        contents.showText(Integer.toString(timeSheet.getMonth().getValue()));//month
        contents.setFont(fontBold, 12);
        contents.showText(" / ");
        contents.setFont(font, 12);
        contents.showText(Integer.toString(timeSheet.getYear()));//year
        contents.newLineAtOffset(-187, -22);
        contents.showText(timeSheet.getUser().realname);//name
        contents.newLineAtOffset(0, -42);
        contents.showText(timeSheet.getRequiredHoursPerMonth() + " Stunden");//required hours per month
        contents.endText();

        yOff = 637;
        int sumHour = 0;
        int sumMin = 0;
        for (TimeRecord timeR : recordsToPDF) {
            contents.beginText();
            contents.setFont(font, 10);
            contents.newLineAtOffset(58, yOff);
            contents.showText(timeR.getDescription());//description
            contents.newLineAtOffset(139, 0);
            contents.showText(timeR.getCategory().getName());//category
            //start date + time
            LocalDateTime startDate = timeR.getBeginning();
            contents.newLineAtOffset(102, 0);
            if (startDate.getDayOfMonth() < 10) {
                contents.showText("0" + startDate.getDayOfMonth() + ".");
            } else {
                contents.showText(startDate.getDayOfMonth() + ".");
            }
            if (startDate.getMonth().getValue() < 10) {
                contents.showText("0" + startDate.getMonth().getValue() + "." + startDate.getYear());
            } else {
                contents.showText(startDate.getMonth().getValue() + "." + startDate.getYear());
            }
            contents.newLineAtOffset(50, 0);
            if (startDate.getHour() < 10) {
                contents.showText(" 0" + startDate.getHour());
            } else {
                contents.showText(" " + startDate.getHour());
            }
            if (startDate.getMinute() < 10) {
                contents.showText(":0" + startDate.getMinute());
            } else {
                contents.showText(":" + startDate.getMinute());
            }
            //end date + time
            LocalDateTime endDate = timeR.getEnding();
            contents.newLineAtOffset(35, 0);
            if (endDate.getDayOfMonth() < 10) {
                contents.showText("0" + endDate.getDayOfMonth() + ".");
            } else {
                contents.showText(endDate.getDayOfMonth() + ".");
            }
            if (endDate.getMonth().getValue() < 10) {
                contents.showText("0" + endDate.getMonth().getValue() + "." + endDate.getYear());
            } else {
                contents.showText(endDate.getMonth().getValue() + "." + endDate.getYear());
            }
            contents.newLineAtOffset(50, 0);
            if (endDate.getHour() < 10) {
                contents.showText(" 0" + endDate.getHour());
            } else {
                contents.showText(" " + endDate.getHour());
            }
            if (endDate.getMinute() < 10) {
                contents.showText(":0" + endDate.getMinute());
            } else {
                contents.showText(":" + endDate.getMinute());
            }
            //total time
            contents.newLineAtOffset(55, 0);
            contents.showText(timeR.getTotHour() + ":");
            if (timeR.getTotMin() < 10) {
                contents.showText("0" + timeR.getTotMin() + "h");
            } else {
                contents.showText(timeR.getTotMin() + "h");
            }
            sumHour += timeR.getTotHour();
            sumMin += timeR.getTotMin();
            contents.endText();
            yOff -= 17;
        }
        contents.beginText();
        contents.newLineAtOffset(384, yOff);
        contents.setFont(fontBold, 10);
        contents.showText("Summe");
        contents.newLineAtOffset(105, 0);
        contents.showText(sumHour + ":");//sum of total time
        if (sumMin < 10) {
            contents.showText("0" + sumMin + "h");
        } else {
            contents.showText(sumMin + "h");
        }
        contents.endText();
        contents.close();
    }

    /**
     * creates one pdf with all given timesheets
     * @param timeSheets list of timesheets
     * @return one pdf with all timesheets
     */
    public File createPdfFromAllTimeSheets(List<TimeSheet> timeSheets) {//TODO test this method
        File file = null;
        PDDocument doc = null;
        PDDocument totDoc = null;

        for (TimeSheet timesheet : timeSheets) {
            File tmp =  createPdfFromTimeSheet(timesheet);
            try {
                doc = PDDocument.load(tmp);
            } catch (Exception e) {
                LOGGER.error("Loading error in createPdfFromAllTimeSheets");
            }
            PDPageTree loopTree =  doc.getPages();
            for (int i = 0; i < loopTree.getCount(); i++) {
                totDoc.addPage(loopTree.get(i));
            }
        }
        return file;
    }

    public static void main(String[] args) {
        MainControl.getInstance().startup(false);
        TimeSheetHandler handler = TimeSheetHandler.getInstance();
        handler.createPdfFromTimeSheet(TimeSheetDAO.getInstance().getTimeSheet(Month.JANUARY, 2016, UserDAO.getInstance().findUser("tom")));

        System.exit(0);
    }
}

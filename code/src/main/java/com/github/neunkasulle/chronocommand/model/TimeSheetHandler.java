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
    //TODO @Dav add new page when timesheet is "full" and check if "tätigkeit" needs a new line
    public File createPdfFromTimeSheet(TimeSheet timeSheet) {
        PDDocument pdfTimeSheet = null;
        File file;
        File returnFile = null;
        try {
            file = new File(getClass().getClassLoader().getResource("Stundenzettel.pdf").getFile());
            //returnFile = new File(getClass().getClassLoader().getResource("Study.pdf").getFile());
            returnFile = new File("C:\\Users\\Dav\\Documents\\ChronoCommand\\code\\src\\main\\resources\\Study.pdf");
            pdfTimeSheet = PDDocument.load(file);
        } catch (Exception e) {
            System.err.println("Loading error.");
            return null;
        }
        try {
            PDPage page = pdfTimeSheet.getPage(0);
            PDPageContentStream contents = new PDPageContentStream(pdfTimeSheet, page, true, true);
            //fillContent(contents, timeSheet);
            //TODO
            List<TimeRecord> recordsToPDF = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

            int yOff = 0;
            PDFont font = PDType1Font.HELVETICA;
            PDFont fontBold = PDType1Font.HELVETICA_BOLD;

            try {
                contents.beginText();
                contents.setFont(font, 12);
                //fill name etc
                contents.newLineAtOffset(465, 764);
                contents.showText(Integer.toString(timeSheet.month.getValue()));//month
                contents.setFont(fontBold, 12);
                contents.showText(" / ");
                contents.setFont(font, 12);
                contents.showText(Integer.toString(timeSheet.year));//year
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
                    contents.newLineAtOffset(60, yOff);
                    contents.showText(timeR.getDescription());//description
                    contents.newLineAtOffset(137, 0);//x = 197 from 0
                    String category = timeR.getCategory().getName();
                    if (category != null) {
                        contents.showText(timeR.getCategory().getName());//category
                    } else {
                        contents.showText("");
                    }
                    //start date + time
                    LocalDateTime startDate = timeR.getBeginning();
                    contents.newLineAtOffset(102, 0);//x = 299 from 0
                    contents.showText(startDate.getDayOfMonth() + "." + startDate.getMonth().getValue() + "." + startDate.getYear());
                    contents.newLineAtOffset(45, 0);
                    contents.showText(" " + startDate.getHour() + ":" + startDate.getMinute());
                    //end date + time
                    LocalDateTime endDate = timeR.getEnd();
                    contents.newLineAtOffset(42, 0);
                    contents.showText(endDate.getDayOfMonth() + "." + endDate.getMonth().getValue() + "." + endDate.getYear());
                    contents.newLineAtOffset(45, 0);
                    contents.showText(" " + endDate.getHour() + ":" + endDate.getMinute());
                    //total time
                    contents.newLineAtOffset(60, 0);
                    contents.showText(timeR.getTotHour() + ":" + timeR.getTotMin() + "h");
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
                contents.showText(sumHour + ":" + sumMin + "h");//sum of total time
                contents.endText();

                contents.close();
            } catch (Exception e) {
                System.out.println("Error in fillContent");
            }
            //TODO

            pdfTimeSheet.save(returnFile);
        } catch (Exception e) {
            System.err.println("problem in content section");
        }
        try {
            pdfTimeSheet.close();
        } catch (Exception e) {
            System.err.println("closing went wrong");
        }
        return returnFile;
    }

    //private void fillContent( PDPageContentStream contents, TimeSheet timeSheet) {

    //    return;
    //}

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
                System.err.println("Loading error in createPdfFromAllTimeSheets");
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

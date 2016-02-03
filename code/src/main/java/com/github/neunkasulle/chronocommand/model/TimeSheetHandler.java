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
    //TODO @Dav add new page when timesheet is "full"
    public File createPdfFromTimeSheet(TimeSheet timeSheet) {
        List<TimeRecord> recordsToPDF = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

        PDDocument pdfTimeSheet = null;
        File file;
        File returnFile = null;
        try {
            file = new File("C:\\Users\\Dav\\Documents\\ChronoCommand\\code\\src\\main\\resources\\Stundenzettel.pdf");
            returnFile = new File("C:\\Users\\Dav\\Documents\\ChronoCommand\\code\\src\\main\\resources\\Study.pdf");
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
            contents.newLineAtOffset(165, 22);
            contents.showText(timeSheet.month.toString());//month
            contents.setFont(fontBold, 12);
            contents.showText(" / ");
            contents.setFont(font, 12);
            contents.showText(Integer.toString(timeSheet.year));//year
            contents.newLineAtOffset(20, -64);
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
                String category = timeR.getCategory().getName();
                if (category != null) {
                    contents.showText(timeR.getCategory().getName());
                } else {
                    contents.showText("");
                }
                contents.endText();
                yOff = yOff - 17;
            }
            //Time bulk, may need no extra offset calculation
            yOff = 637;
            int sumHour = 0;
            int sumMin = 0;
            for (TimeRecord timeR : recordsToPDF) {
                contents.beginText();
                contents.newLineAtOffset(299, yOff);
                contents.showText(timeR.getBeginning().toString());//start
                contents.newLineAtOffset(85, 0);
                contents.showText(timeR.getEnd().toString());//end
                contents.newLineAtOffset(105, 0);
                contents.showText(timeR.getTotHour() + ":" + timeR.getTotMin() + "h");//total time
                contents.endText();
                sumHour += timeR.getTotHour();
                sumMin += timeR.getTotMin();
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
        MainControl.getInstance().startup();
        TimeSheetHandler handler = TimeSheetHandler.getInstance();
        handler.createPdfFromTimeSheet(TimeSheetDAO.getInstance().getTimeSheet(Month.JANUARY, 2016, UserDAO.getInstance().findUser("tom")));

        System.exit(0);
    }

}

package com.github.neunkasulle.chronocommand.model;


import com.github.neunkasulle.chronocommand.control.TimeSheetControl;

import java.time.LocalDate;

/**
 * nicht überprüft werden Ruhezeiteinhaltungen, da es bis jz keine sinnvolle möglichkeit gibt
 * das zu überprüfen.
 */
public class GermanLawRegulations extends Regulations {

    @Override
    public String checkTimeSheet(TimeSheet timeSheet) {
        TimeSheetDAO timeSheetDAO = new TimeSheetDAO();
        TimeRecord[] timeRecords = timeSheetDAO.getTimeRecords(timeSheet);


        return super.checkTimeSheet(timeSheet);
    }

    /**
     * überprüfen, dass nicht zwischen 23 und 6 uhr gearbeitet wird
     * und es darf auch nicht mehr als 2 stunden in die nachtzeit
     * hineingearbeitet werden
     * @param timeRecords
     * @param timeSheet
     * @return "" für ok, Fehlermeldung sonst
     */
    private String checkNightWork(TimeRecord[] timeRecords, TimeSheet timeSheet) {
        String result = "";
        for (TimeRecord timeRecord: timeRecords) {
            //FIXME
            if (false) {
                if (timeRecord.beginning <= 6 || timeRecord.beginning >= 23) {
                    result += "Nachtarbeit nicht erlaubt";
                } else if (timeRecord.end > 1) {
                    result += "Nachtarbeit nicht erlaubt";
                } else if (timeRecord.beginning < 4) {
                    result += "Nachtarbeit nicht erlaubt";
                }
            }
        }
        return result;
    }

    /**
     * Überprüfen, dass nach 6h Arbeit eine Pause eingelegt wird
     * @param timeRecords
     * @param timeSheet
     * @return
     */
    private String checkForPauses(TimeRecord[] timeRecords, TimeSheet timeSheet) {
        String result = "";
        for (TimeRecord timeRecord: timeRecords) {
            if (timeRecord.end - timeRecord.beginning >= 6) {
                result += "Nach 6h Arbeiten muss eine Pause eingelegt werden";
            }
        }
        return result;
    }

    /**
     * Überprüfen, dass nicht unerlausbt an Sonn- und Feiertagen gearbeitet wird (BaWÜ)
     * @param timeRecords
     * @param timeSheet
     * @return
     */
    private String checkSundayWork(TimeRecord[] timeRecords, TimeSheet timeSheet) {
        String result = "";
        for (TimeRecord timeRecord: timeRecords) {
            //FIXME
            if (false) {
                if (timeRecord.beginning == 1 /* onsunday */) {
                    result += "Sonn- und Feiertagsarbeit nicht erlaubt";
                }
                else if (timeRecord.end == 1 /*onsunday*/) {
                    result += "Sonn- und Feiertagsarbeit nicht erlaubt";
                }
            }
        }
        return result;
    }

    /**
     * überprüfen, dass eine person nicht mehr als 8 (10) h am Tag
     * arbeitet
     * @param timeSheet
     * @return
     */
    private String checkWorkHours (TimeSheet timeSheet) {
        TimeSheetDAO timeSheetDAO = new TimeSheetDAO();
        String result = "";
        for (int n = 1; n <= getNumberOfDays(timeSheet); n++) {

            TimeRecord[] timeRecords = timeSheetDAO.getTimeRecordsByDay(timeSheet, n);
            int hoursPerDay = 0;
            for (TimeRecord timeRecord : timeRecords ) {
                hoursPerDay += timeRecord.end - timeRecord.beginning;
            }
            if (!timeSheet.proletarier.longHours) {
                if (hoursPerDay > 8) {
                    result += "Maximale Arbeitszeit überschritten";
                }
            }
            else
                if (hoursPerDay >10) {
                    result += "Maximale Arbeitszeit überschritten";
                }
        }
        return result;
    }

    /**
     * anzahl der Tage im aktuellen Monat berechne
     * @param timeSheet
     * @return,
     * noch falsch plaziert, muss in ne andere klasse. weiß noch nicht welche
     * ich denke, dass das leider mit switch case machen muss ^^
     */
    private int getNumberOfDays(TimeSheet timeSheet) {
        int days = LocalDate.of(timeSheet.year, timeSheet.month, 1).lengthOfMonth();
        return days;
    }

    /**
     * überprüfen, dass nicht mehr als die vertraglcih festgelegte zeit pro monat
     * gearbeitet wird.
     * @param timeRecords
     * @param timeSheet
     * @return
     */
    private String checkMonthHours(TimeRecord[] timeRecords, TimeSheet timeSheet) {
        String result = "";
        if ( timeSheet.currentHours > timeSheet.requiredHoursPerMonth) {
            result += "Maximale Arbeitszeit für diesen Monat erreicht";
        }
        return result;
    }

}

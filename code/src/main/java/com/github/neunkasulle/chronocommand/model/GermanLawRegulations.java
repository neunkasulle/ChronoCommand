package com.github.neunkasulle.chronocommand.model;


import java.time.*;
import java.util.List;

/**
 * nicht überprüft werden Ruhezeiteinhaltungen, da es bis jz keine sinnvolle möglichkeit gibt
 * das zu überprüfen.
 */
public class GermanLawRegulations extends Regulations {

    @Override
    public String checkTimeSheet(TimeSheet timeSheet) {
        List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

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
            if (timeRecord.getBeginning().isBefore(LocalDateTime.of(timeRecord.getBeginning().getYear(), timeRecord.getBeginning().getMonth(),
                    timeRecord.getBeginning().getDayOfMonth(), 6, 0)) || timeRecord.getBeginning().isAfter(LocalDateTime.of(timeRecord.getBeginning().getYear(),
                    timeRecord.getBeginning().getMonth(), timeRecord.getBeginning().getDayOfMonth(), 23, 0))) {
                result += "Nachtarbeit nicht erlaubt";
            } else if (timeRecord.getEnd().isAfter(LocalDateTime.of(timeRecord.getEnd().getYear(), timeRecord.getEnd().getMonth(), timeRecord.getEnd().getDayOfMonth(), 1, 0))
                    && timeRecord.getEnd().isBefore(LocalDateTime.of(timeRecord.getEnd().getYear(), timeRecord.getEnd().getMonth(), timeRecord.getEnd().getDayOfMonth(), 6, 0))) {
                result += "Nachtarbeit nicht erlaubt";
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

        int maxWorkingWithoutBreak = 6*60*60; // 6 Hours in seconds

        for (TimeRecord timeRecord : timeRecords) {
            if (timeRecord.getEnd().toEpochSecond(ZoneOffset.UTC) - timeRecord.getBeginning().toEpochSecond(ZoneOffset.UTC) >= maxWorkingWithoutBreak) {
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
        for (TimeRecord timeRecord : timeRecords) {
            if (timeRecord.getBeginning().getDayOfWeek() == DayOfWeek.SUNDAY) {
                result += "Sonn- und Feiertagsarbeit nicht erlaubt";
            }
            else if (timeRecord.getEnd().getDayOfWeek() == DayOfWeek.SUNDAY) {
                result += "Sonn- und Feiertagsarbeit nicht erlaubt";
            }
            //TODO: Feiertage einlesen und hier dann überprüfen lassen
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
        int maxWorkingHours = 8*60*60; // 8 Hours in seconds
        int maxOvertime = 10*60*60; // 2 Hours in seconds

        String result = "";
        for (int n = 1; n <= getNumberOfDaysInMonth(timeSheet); n++) {

            List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecordsByDay(timeSheet, n);
            int secondsPerDay = 0;
            for (TimeRecord timeRecord : timeRecords ) {
                secondsPerDay += timeRecord.getEnd().toEpochSecond(ZoneOffset.UTC) - timeRecord.getBeginning().toEpochSecond(ZoneOffset.UTC);
            }
            if (!timeSheet.user.isPermitted("longHours")) {
                if (secondsPerDay > maxWorkingHours) {
                    result += "Maximale Arbeitszeit überschritten";
                }
            }
            else
                if (secondsPerDay > maxWorkingHours + maxOvertime) {
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
    private int getNumberOfDaysInMonth(TimeSheet timeSheet) {
        return LocalDate.of(timeSheet.year, timeSheet.month, 1).lengthOfMonth();
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

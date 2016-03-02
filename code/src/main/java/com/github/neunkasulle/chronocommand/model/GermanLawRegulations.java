package com.github.neunkasulle.chronocommand.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * nicht überprüft werden Ruhezeiteinhaltungen, da es bis jz keine sinnvolle möglichkeit gibt
 * das zu überprüfen.
 */
public class GermanLawRegulations extends Regulations {
    private Map<LocalDate, String> holidays = new HashMap<>();
    private Set<Integer> yearsInitialized = new HashSet<>();

    public GermanLawRegulations() {
        readHolidays(LocalDate.now().getYear());
    }

    @Override
    public String checkTimeSheet(TimeSheet timeSheet) {
        List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

        String result = checkNightWork(timeRecords, timeSheet);
        result += checkForPauses(timeRecords, timeSheet);
        result += checkSundayWork(timeRecords, timeSheet);
        result += checkWorkHours(timeSheet);
        result += checkMonthHours(timeRecords, timeSheet);
        return result;
    }

    /**
     * überprüfen, dass nicht zwischen 23 und 6 uhr gearbeitet wird
     * und es darf auch nicht mehr als 2 stunden in die nachtzeit
     * hineingearbeitet werden
     * @param timeRecords
     * @param timeSheet
     * @return "" für ok, Fehlermeldung sonst
     */
    private String checkNightWork(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        String result = "";
        for (TimeRecord timeRecord: timeRecords) {
            if (timeRecord.getBeginning().isBefore(LocalDateTime.of(timeRecord.getBeginning().getYear(), timeRecord.getBeginning().getMonth(),
                    timeRecord.getBeginning().getDayOfMonth(), 6, 0)) || timeRecord.getBeginning().isAfter(LocalDateTime.of(timeRecord.getBeginning().getYear(),
                    timeRecord.getBeginning().getMonth(), timeRecord.getBeginning().getDayOfMonth(), 23, 0))) {
                result += "Nachtarbeit nicht erlaubt";
            } else if (timeRecord.getEnding().isAfter(LocalDateTime.of(timeRecord.getEnding().getYear(), timeRecord.getEnding().getMonth(), timeRecord.getEnding().getDayOfMonth(), 1, 0))
                    && timeRecord.getEnding().isBefore(LocalDateTime.of(timeRecord.getEnding().getYear(), timeRecord.getEnding().getMonth(), timeRecord.getEnding().getDayOfMonth(), 6, 0))) {
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
    private String checkForPauses(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        String result = "";

        int maxWorkingWithoutBreak = 6*60; // 6 Hours in minutes

        for (TimeRecord timeRecord : timeRecords) {
            if (ChronoUnit.MINUTES.between(timeRecord.getBeginning(), timeRecord.getEnding()) >= maxWorkingWithoutBreak) {
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
    private String checkSundayWork(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        readHolidays(timeSheet.getYear());

        String result = "";
        for (TimeRecord timeRecord : timeRecords) {
            if (timeRecord.getBeginning().getDayOfWeek() == DayOfWeek.SUNDAY) {
                result += "Sonn- und Feiertagsarbeit nicht erlaubt";
            }
            else if (timeRecord.getEnding().getDayOfWeek() == DayOfWeek.SUNDAY) {
                result += "Sonn- und Feiertagsarbeit nicht erlaubt";
            }
            else if (holidays.containsKey(timeRecord.getBeginning().toLocalDate())) {
                result += "Sonn- und Feiertagsarbeit nicht erlaubt";
            }
            else if (holidays.containsKey(timeRecord.getEnding().toLocalDate())) {
                result += "Sonn- und Feiertagsarbeit nicht erlaubt";
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
        int maxWorkingHours = 8*60; // 8 Hours in minutes
        int maxOvertime = 2*60; // 2 Hours in minutes

        String result = "";
        for (int n = 1; n <= getNumberOfDaysInMonth(timeSheet); n++) {

            List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecordsByDay(timeSheet, n);
            int minutesPerDay = 0;
            for (TimeRecord timeRecord : timeRecords ) {
                minutesPerDay += ChronoUnit.MINUTES.between(timeRecord.getBeginning(), timeRecord.getEnding());
            }
            if (!timeSheet.getUser().isPermitted(Role.PERM_LONGHOURS)) {
                if (minutesPerDay > maxWorkingHours) {
                    result += "Maximale Arbeitszeit überschritten";
                }
            }
            else
                if (minutesPerDay > maxWorkingHours + maxOvertime) {
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
        return LocalDate.of(timeSheet.getYear(), timeSheet.getMonth(), 1).lengthOfMonth();
    }

    /**
     * überprüfen, dass nicht mehr als die vertraglcih festgelegte zeit pro monat
     * gearbeitet wird.
     * @param timeRecords
     * @param timeSheet
     * @return
     */
    private String checkMonthHours(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        String result = "";
        if ( timeSheet.getCurrentMinutesThisMonth() > timeSheet.getRequiredHoursPerMonth() * 60) {
            result += "Maximale Arbeitszeit für diesen Monat überschritten";
        }
        return result;
    }

    private void readHolidays(int year) {
        if (yearsInitialized.contains(year)) {
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource("feiertage-bw.bin").getPath()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] tmp = line.split(":");
                    if (tmp.length != 2) {
                        throw new Exception();
                    }
                    String holidayName = tmp[0].trim();
                    String dateStr = tmp[1].trim();
                    if (dateStr.charAt(0) == '+' || dateStr.charAt(0) == '-') {
                        int days = Integer.parseInt(dateStr.substring(1));
                        LocalDate date = getEasterDate(year);
                        if (dateStr.charAt(0) == '+') {
                            date.plusDays(days);
                        } else {
                            date.minusDays(days);
                        }
                        holidays.put(date, holidayName);
                    } else {
                        String[] daymonth = dateStr.split("\\.");
                        int day = Integer.parseInt(daymonth[0]);
                        int month = Integer.parseInt(daymonth[1]);
                        LocalDate date = LocalDate.of(year, month, day);
                        holidays.put(date, holidayName);
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }
        yearsInitialized.add(year);
    }

    public LocalDate getEasterDate(int year) throws ChronoCommandException {
        switch (year) {
            case 2015:
                return LocalDate.of(2015, 4, 5);
            case 2016:
                return LocalDate.of(2016, 3, 27);
            case 2017:
                return LocalDate.of(2017, 4, 16);
            case 2018:
                return LocalDate.of(2018, 4, 1);
            case 2019:
                return LocalDate.of(2019, 4, 21);
            case 2020:
                return LocalDate.of(2020, 4, 12);
            default:
                throw new ChronoCommandException(Reason.INVALIDNUMBER);
        }
    }
}

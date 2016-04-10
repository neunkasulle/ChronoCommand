package com.github.neunkasulle.chronocommand.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * nicht überprüft werden Ruhezeiteinhaltungen, da es bis jz keine sinnvolle möglichkeit gibt
 * das zu überprüfen.
 */
public class GermanLawRegulations extends Regulations {
    private Map<LocalDate, String> holidays = new HashMap<>();
    private Set<Integer> yearsInitialized = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSheetHandler.class);
    public GermanLawRegulations() {
        readHolidays(LocalDate.now().getYear());
    }

    @Override
    public List<RegulationRejectionReason> checkTimeSheet(TimeSheet timeSheet) {
        List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecords(timeSheet);

        List<RegulationRejectionReason> result = checkNightWork(timeRecords, timeSheet);
        result.addAll(checkForPauses(timeRecords, timeSheet));
        result.addAll(checkSundayWork(timeRecords, timeSheet));
        result.addAll(checkWorkHours(timeSheet));
        //result += checkMonthHours(timeRecords, timeSheet);
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
    private List<RegulationRejectionReason> checkNightWork(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        List<RegulationRejectionReason> result = new LinkedList<RegulationRejectionReason>();
        for (TimeRecord timeRecord: timeRecords) {
            if (timeRecord.getBeginning().isBefore(LocalDateTime.of(timeRecord.getBeginning().getYear(), timeRecord.getBeginning().getMonth(),
                    timeRecord.getBeginning().getDayOfMonth(), 6, 0)) || timeRecord.getBeginning().isAfter(LocalDateTime.of(timeRecord.getBeginning().getYear(),
                    timeRecord.getBeginning().getMonth(), timeRecord.getBeginning().getDayOfMonth(), 23, 0))) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.NIGHTWORK));
            } else if (timeRecord.getEnding().isAfter(LocalDateTime.of(timeRecord.getEnding().getYear(), timeRecord.getEnding().getMonth(), timeRecord.getEnding().getDayOfMonth(), 1, 0))
                    && timeRecord.getEnding().isBefore(LocalDateTime.of(timeRecord.getEnding().getYear(), timeRecord.getEnding().getMonth(), timeRecord.getEnding().getDayOfMonth(), 6, 0))) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.NIGHTWORK));
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
    private List<RegulationRejectionReason> checkForPauses(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        List<RegulationRejectionReason> result = new LinkedList<>();

        int maxWorkingWithoutBreak = 6*60; // 6 Hours in minutes

        for (TimeRecord timeRecord : timeRecords) {
            if (ChronoUnit.MINUTES.between(timeRecord.getBeginning(), timeRecord.getEnding()) >= maxWorkingWithoutBreak) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.BREAK));
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
    private List<RegulationRejectionReason> checkSundayWork(List<TimeRecord> timeRecords, TimeSheet timeSheet) {
        readHolidays(timeSheet.getYear());

        List<RegulationRejectionReason> result = new LinkedList<>();
        for (TimeRecord timeRecord : timeRecords) {
            if (timeRecord.getBeginning().getDayOfWeek() == DayOfWeek.SUNDAY) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.SUNDAY_WORK));
            }
            else if (timeRecord.getEnding().getDayOfWeek() == DayOfWeek.SUNDAY) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.SUNDAY_WORK));
            }
            else if (holidays.containsKey(timeRecord.getBeginning().toLocalDate())) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.HOLIDAY_WORK));
            }
            else if (holidays.containsKey(timeRecord.getEnding().toLocalDate())) {
                result.add(new RegulationRejectionReason(timeRecord, RegulationRejectionReason.RejectionReason.HOLIDAY_WORK));
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
    private List<RegulationRejectionReason> checkWorkHours (TimeSheet timeSheet) {
        int maxWorkingHours = 8*60; // 8 Hours in minutes
        int maxOvertime = 2*60; // 2 Hours in minutes

        List<RegulationRejectionReason> result = new LinkedList<>();
        for (int n = 1; n <= getNumberOfDaysInMonth(timeSheet); n++) {

            List<TimeRecord> timeRecords = TimeSheetDAO.getInstance().getTimeRecordsByDay(timeSheet, n);
            int minutesPerDay = 0;
            for (TimeRecord timeRecord : timeRecords ) {
                minutesPerDay += ChronoUnit.MINUTES.between(timeRecord.getBeginning(), timeRecord.getEnding());
            }
            if (!timeSheet.getUser().isPermitted(Role.PERM_LONGHOURS)) {
                if (minutesPerDay > maxWorkingHours) {
                    result.add(new RegulationRejectionReason(timeRecords.get(0), RegulationRejectionReason.RejectionReason.MAXIMUM_WORKTIME));
                }
            }
            else
                if (minutesPerDay > maxWorkingHours + maxOvertime) {
                    result.add(new RegulationRejectionReason(timeRecords.get(0), RegulationRejectionReason.RejectionReason.MAXIMUM_WORKTIME));
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
            LOGGER.error("Error while parsing:Holiday file", e);
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

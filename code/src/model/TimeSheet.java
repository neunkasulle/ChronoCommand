package model;

/**
 * Created by Janze on 16.01.2016.
 */
public class TimeSheet {
    int id;
    Proletarier proletarier;
    TimeSheetState state;
    int month;
    int year;
    int hoursPerMonth;

    public void addTime(TimeRecord timeRecord) {

    }
    public boolean setTimeSheetState(TimeSheetState state) {
        return false;
    }
}

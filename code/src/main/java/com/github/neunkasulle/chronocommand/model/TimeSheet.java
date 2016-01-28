package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by Janze on 16.01.2016.
 */
@Entity
@Table(name="timeSheet")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class TimeSheet {

    @Id
    @GeneratedValue
    int id;

    @Basic(optional = false)
    User proletarier;

    @Basic(optional = false)
    TimeSheetState state;

    @Basic(optional = false)
    int month; // 1-12

    @Basic(optional = false)
    int year;

    @Basic(optional = false)
    int requiredHoursPerMonth;

    int currentHours;

    public TimeSheet(User proletarier, int month, int year, int hoursPerMonth) {
        this.proletarier = proletarier;
        this.month = month;
        this.year = year;
        this.requiredHoursPerMonth = hoursPerMonth;
        state = TimeSheetState.UNLOCKED;
    }

    public void addTime(TimeRecord timeRecord) {

    }

    public void setCurrentHours(int hours) {
        currentHours = hours;
    }

    public boolean setTimeSheetState(TimeSheetState state) {
        return false;
    }

    public User getProletarier() { return proletarier; }
}

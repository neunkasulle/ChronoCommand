package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

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


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Basic(optional = false)
    TimeSheetState state;

    @Basic(optional = false)
    int month; // 1-12

    @Basic(optional = false)
    int year;

    @Basic(optional = false)
    int requiredHoursPerMonth;

    int currentHours;

    public TimeSheet() {

    }

    public TimeSheet(User proletarier, int month, int year, int hoursPerMonth) {
        this.user = proletarier;
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
        this.state = state;

        return true;
    }

    public User getUser() {
        return user;
    }

    public TimeSheetState getState() {
        return state;
    }
}

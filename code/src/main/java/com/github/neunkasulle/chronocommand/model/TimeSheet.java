package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.Month;


/**
 * Created by Janze on 16.01.2016.
 *
 */
@Entity
@Table(name="cc_timesheets")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class TimeSheet {

    @Id
    @GeneratedValue
    int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    User user;

    @Basic(optional = false)
    TimeSheetState state;

    @Basic(optional = false)
    Month month;

    @Basic(optional = false)
    int year;

    @Basic(optional = false)
    int requiredHoursPerMonth;

    /*@OneToMany
    List<TimeRecord> timeRecords;*/

    int currentMinutesThisMonth;

    public TimeSheet() {}

    public TimeSheet(User user, Month month, int year) {
        this.user = user;
        this.month = month;
        this.year = year;
        this.requiredHoursPerMonth = this.user.getHoursPerMonth();
        state = TimeSheetState.UNLOCKED;
        //timeRecords = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setCurrentMinutesThisMonth(int hours) {
        currentMinutesThisMonth = hours;
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

    /*public void addTime(TimeRecord timeRecord) {
        timeRecords.add(timeRecord);
    }*/
    
}

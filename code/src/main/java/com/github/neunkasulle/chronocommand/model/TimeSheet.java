package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Janze on 16.01.2016.
 *
 */
@Entity
@Table(name="cc_timesheets")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class TimeSheet implements Comparable<TimeSheet> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated
    @JoinTable(name = "cc_timesheet_messages")
    private List<Message> messages;

    @Basic(optional = false)
    private TimeSheetState state;

    @Basic(optional = false)
    private Month month;

    @Basic(optional = false)
    private int year;

    @Basic(optional = false)
    private int requiredHoursPerMonth;

    private int currentMinutesThisMonth = 0;

    protected TimeSheet() {
        // hibernate needs this
    }

    public TimeSheet(User user, Month month, int year) throws ChronoCommandException {
        if (user == null) {
            throw new ChronoCommandException(Reason.NOSUCHUSER);
        }
        this.user = user;
        this.month = month;
        this.year = year;
        this.requiredHoursPerMonth = this.user.getHoursPerMonth();
        this.state = TimeSheetState.UNLOCKED;
        this.messages = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public int getCurrentMinutesThisMonth() {
        return currentMinutesThisMonth;
    }

    public void setCurrentMinutesThisMonth(int currentMinutesThisMonth) {
        this.currentMinutesThisMonth = currentMinutesThisMonth;
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

    public Month getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public int getRequiredHoursPerMonth() {
        return this.requiredHoursPerMonth;
    }


    @Override
    public String toString() {
        String monthStr;

        monthStr = month.toString();
        return monthStr + " " + year;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessage(Message message) {
        this.messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TimeSheet && id.equals(((TimeSheet) o).getId());
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public int compareTo(TimeSheet comparedTimeSheet) {

        return this.getLocalDate().compareTo(comparedTimeSheet.getLocalDate());
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(this.getYear(), this.getMonth(), 1);
    }
}

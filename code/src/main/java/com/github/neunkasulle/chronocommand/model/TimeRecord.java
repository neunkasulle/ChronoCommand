package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Janze on 16.01.2016.
 * A single Time Record
 */
@Entity
@Table(name = "cc_timerecords")
public class TimeRecord {
    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    private LocalDateTime beginning;

    @Column
    private LocalDateTime ending;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Basic
    private String description;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private TimeSheet timeSheet;

    public TimeRecord() {
        // hibernate needs this
    }

    /**
     * Constructs a new time record.
     *
     * @param begin       The beginning of the time record
     * @param ending         The end of the time record
     * @param category    the category in which this record can be sorted
     * @param description a short description of this recorded time
     */
    public TimeRecord(LocalDateTime begin, LocalDateTime ending, Category category, String description, TimeSheet timeSheet) {
        setBeginning(begin);
        setEnding(ending);
        setCategory(category);
        setDescription(description);
        this.timeSheet = timeSheet;
    }


    /**
     * Gets the internal DB id
     *
     * @return the internal DB id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the beginning time of a record
     *
     * @return the beginning time
     */
    public LocalDateTime getBeginning() {
        return beginning;
    }

    /**
     * Returns the beginning hour only
     * @return the beginning hour
     */
    public int getBeginningHour() {
        return this.beginning.getHour();
    }

    /**
     * Updates the beginning hour
     * @param hour the nre hour
     */
    public void setBeginningHour(final int hour) {
        this.beginning = this.beginning.minusHours(this.beginning.getHour()).plusHours(hour);
    }

    /**
     * Returns the beginning minute only
     * @return the beginning minute
     */
    public int getBeginningMinute() {
        return this.beginning.getMinute();
    }

    /**
     * Updates the beginning hour
     * @param minute the new minute
     */
    public void setBeginningMinute(final int minute) {
        this.beginning = this.beginning.minusMinutes(this.beginning.getMinute()).plusMinutes(minute);
    }

    /**
     * Gets the ending time of a time record
     *
     * @return the ending time
     */
    public LocalDateTime getEnding() {
        return ending;
    }

    /**
     * Returns the end hour only
     * @return the end hour
     */
    public int getEndHour() {
        return this.ending.getHour();
    }

    /**
     * Updates the end hour
     * @param hour the nre hour
     */
    public void setEndHour(final int hour) {
        this.ending = this.ending.minusHours(this.ending.getHour()).plusHours(hour);
    }

    /**
     * Returns the end minute only
     * @return the end minute
     */
    public int getEndMinute() {
        return this.ending.getMinute();
    }

    /**
     * Updates the end hour
     * @param minute the new minute
     */
    public void setEndMinute(final int minute) {
        this.ending = this.ending.minusMinutes(this.ending.getMinute()).plusMinutes(minute);
    }

    /**
     * Small sanity checks for the time record
     *
     * @return false, if something is bogus with the times
     */
    public boolean checkTimes() {
        return false;
    }

    /**
     * Gets the category of the time record
     *
     * @return the category of the time record
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the time record, old category will be purged
     *
     * @param category the new category for this time record
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets the small description of the time record
     *
     * @return description of the time record
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the time record, old description will be pruged
     *
     * @param description the new description for a time record
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets a new end time for this time record
     *
     * @param end the new end time
     */
    public void setEnding(LocalDateTime end) {
        this.ending = end.withSecond(0).withNano(0);
    }

    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public int getTotHour() {
        return  this.getEndHour() - this.getBeginningHour();
    }

    public int getTotMin() {
        return this.getEndMinute() - this.getBeginningMinute();
    }

    public void setBeginning(LocalDateTime beginning) {
        this.beginning = beginning.withNano(0).withSecond(0);
    }
}

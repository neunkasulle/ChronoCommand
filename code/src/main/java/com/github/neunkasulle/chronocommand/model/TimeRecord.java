package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Janze on 16.01.2016.
 * A single Time Record
 */
@Entity
@Table(name = "cc_timerecords")
public class TimeRecord implements Comparable<TimeRecord> {
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
     * Gets the ending time of a time record
     *
     * @return the ending time
     */
    public LocalDateTime getEnding() {
        return ending;
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
        if (end != null) {
            this.ending = end.withSecond(0).withNano(0);
        } else {
            this.ending = null;
        }
    }

    // needed by bean container for time record editing
    public int getBeginningHour() {
        return beginning.getHour();
    }

    // needed by bean container for time record editing
    public void setBeginningHour(int hour) {
        beginning = beginning.withHour(hour);
    }

    // needed by bean container for time record editing
    public int getBeginningMinute() {
        return beginning.getMinute();
    }

    // needed by bean container for time record editing
    public void setBeginningMinute(int minute) {
        beginning = beginning.withMinute(minute);
    }

    // needed by bean container for time record editing
    public int getEndHour() {
        return ending.getHour();
    }

    // needed by bean container for time record editing
    public void setEndHour(int hour) {
        ending = ending.withHour(hour);
    }

    // needed by bean container for time record editing
    public void setEndMinute(int minute) {
        ending = ending.withMinute(minute);
    }

    // needed by bean container for time record editing
    public int getEndMinute() {
        return ending.getMinute();
    }

    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public int getTotalHours() {
        return  ending.getHour() - beginning.getHour();
    }

    public int getTotalMinutes() {
        return ending.getMinute() - beginning.getMinute();
    }

    public void setBeginning(LocalDateTime beginning) {
        this.beginning = beginning.withNano(0).withSecond(0);
    }

    @Override
    public int compareTo(TimeRecord timeRecord) {
        return this.getBeginning().compareTo(timeRecord.getBeginning());
    }
}

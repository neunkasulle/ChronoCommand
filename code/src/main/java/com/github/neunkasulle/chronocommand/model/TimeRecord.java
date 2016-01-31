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
    int id;
    @Column
    LocalDateTime beginning;

    @Column
    LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column
    String description;

    /**
     * Constructs a new time record.
     * @param beginn The beginning of the time record
     * @param end The end of the time record
     * @param category the category in which this record can be sorted
     * @param description a short description of this recorded time
     */
    public TimeRecord(LocalDateTime beginn, LocalDateTime end, Category category, String description) {
        this.beginning = beginn;
        this.end = end;
        this.category = category;
        this.description = description;
    }

    public TimeRecord() {

    }

    /**
     * Gets the internal DB id
     * @return the internal DB id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the beginning time of a record
     * @return the beginning time
     */
    public LocalDateTime getBeginning() {
        return beginning;
    }

    /**
     *  Gets the ending time of a time record
     * @return the ending time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Small sanity checks for the time record
     * @return false, if something is bogus with the times
     */
    public boolean checkTimes() {
        return false;
    }

    /**
     * Gets the category of the time record
     * @return the category of the time record
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the time record, old category will be purged
     * @param category the new category for this time record
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets the small description of the time record
     * @return description of the time record
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the time record, old description will be pruged
     * @param description the new description for a time record
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets a new end time for this time record
     * @param end the new end time
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}

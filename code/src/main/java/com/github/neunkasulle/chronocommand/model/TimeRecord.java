package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Janze on 16.01.2016.
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

    public TimeRecord(LocalDateTime beginn, LocalDateTime end, Category category, String description) {
        this.beginning = beginn;
        this.end = end;
        this.category = category;
        this.description = description;
    }

    public TimeRecord() {

    }


    public int getId() {
        return id;
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean checkTimes() {
        return false;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}

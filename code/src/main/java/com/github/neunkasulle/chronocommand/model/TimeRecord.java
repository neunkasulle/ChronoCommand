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
    private Long id;

    @Basic(optional=false)
    private LocalDateTime beginning;

    @Column
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @Basic
    private String description;

    public TimeRecord(LocalDateTime begin, LocalDateTime end, Category category, String description) {
        this.beginning = begin;
        this.end = end;
        this.category = category;
        this.description = description;
    }

    public TimeRecord() {

    }

    public Long getId() {
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

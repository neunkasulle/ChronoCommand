package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;

/**
 * Created by Janze on 16.01.2016.
 * Categries to choose from for describing work on timeSheets
 */
@Entity
@Table(name = "cc_categories")
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    protected Category() {

    }

    /**
     * Contructs a new category
     * @param name The name of the new category
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the category
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * toString implematation for category
     * @return string of the category DB value
     */
    @Override
    public String toString() {
        return super.toString() + " " + id + " " + name;
    }
}

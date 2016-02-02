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
     * @return unique id of the category set by the database
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the name of the category
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * setName implementation for category
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * toString implematation for category
     * @return string of the category DB value
     */
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Category) {
            Category other = (Category) o;
            return other.getId().equals(id) && other.getName().equals(name);
        } else {
            return false;
        }
    }
}

package com.github.neunkasulle.chronocommand.model;

import javax.persistence.*;

/**
 * Created by Janze on 16.01.2016.
 */
@Entity
@Table(name = "cc_categories")
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    protected Category() {}

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString() + " " + id + " " + name;
    }
}

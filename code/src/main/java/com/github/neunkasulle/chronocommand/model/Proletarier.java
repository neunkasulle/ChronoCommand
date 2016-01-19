package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public class Proletarier extends User {
    String fullName;
    Supervisor supervisor;
    int hoursPerMonth;

    public Proletarier(String username, String email, String password, String fullName, Supervisor supervisor, int hoursPerMonth) {
        super(username, email, password);
        role = Role.PROLETARIER;
        this.fullName = fullName;
        this.supervisor = supervisor;
        this.hoursPerMonth = hoursPerMonth;
    }
}

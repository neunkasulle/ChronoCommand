package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public class Proletarier extends User {
    String fullName;
    Supervisor supervisor;
    int requiredHoursPerMonth;
    boolean nightWork;
    boolean longHours;
    boolean sundayWork;  //auch Feiertage gemein, aber englischer begriff doof
    boolean overtimeWork;  // überstunden und stunden schieben


    public Proletarier(String username, String email, String password, String fullName, Supervisor supervisor, int requiredHoursPerMonth) {
       // super(username, email, password);
       // role = Role.PROLETARIER;
        this.fullName = fullName;
        this.supervisor = supervisor;
        this.requiredHoursPerMonth = requiredHoursPerMonth;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }
}

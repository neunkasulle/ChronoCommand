package com.github.neunkasulle.chronocommand.model;


/**
 * Created by Janze on 30.01.2016.
 * Custum Exception for internal errors inside ChronoCommand
 */
public class ChronoCommandException extends Exception {
    private final Reason reason;

    public ChronoCommandException(Reason reason) {
        super();
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}

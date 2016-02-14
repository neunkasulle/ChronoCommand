package com.github.neunkasulle.chronocommand.model;


/**
 * Created by Janze on 30.01.2016.
 * Custum Exception for internal errors inside ChronoCommand
 */
public class ChronoCommandException extends Exception {
    private final Reason reason;

    private final String details;

    public ChronoCommandException(Reason reason) {
        super();
        this.reason = reason;
        this.details = "";
    }

    public ChronoCommandException(Reason reason, String details) {
        super();
        this.reason = reason;
        this.details = details;
    }

    public Reason getReason() {
        return reason;
    }

    public String getDetails() {
        return details;
    }
}

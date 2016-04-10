package com.github.neunkasulle.chronocommand.model;


/**
 * Created by Janze on 30.01.2016.
 * Custum Exception for internal errors inside ChronoCommand
 */
public class ChronoCommandException extends Exception {
    private final Reason reason;
    private final String details;
    private final Object object;

    public ChronoCommandException(Reason reason) {
        super();
        this.reason = reason;
        this.details = "";
        this.object = null;
    }

    public ChronoCommandException(Reason reason, String details) {
        super();
        this.reason = reason;
        this.details = details;
        this.object = null;
    }

    public ChronoCommandException(Reason reason, Object object) {
        super();
        this.reason = reason;
        this.details = "";
        this.object = object;
    }

    public Reason getReason() {
        return reason;
    }

    public String getDetails() {
        return details;
    }

    public Object getObject() {
        return object;
    }
}

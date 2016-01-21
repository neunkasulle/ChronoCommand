package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Handles User sessions
 */
public class SessionControl extends Control {
    private int numberOfSessions;
    private static SessionControl ourInstance = new SessionControl();

    private SessionControl() {

    }

    public static SessionControl getInstance() {
        return ourInstance;
    }

    private boolean killSessions() {

        return false;
    }

    public void timeEvent() {
        throw new NotYetImplementedException();
    }

    private void sessionTimeout() {
        throw new NotYetImplementedException();
    }


}

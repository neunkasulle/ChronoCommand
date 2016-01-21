package com.github.neunkasulle.chronocommand.control;

/**
 * Created by Janze on 18.01.2016.
 */
public class SessionControl extends Control {

    private static SessionControl ourInstance = new SessionControl();

    public static SessionControl getInstance() {
        return ourInstance;
    }

    private SessionControl() {

    }

    private int numberOfSessions;

    private boolean killSessions() {

        return false;
    }

    public void timeEvent() {

    }

    private void sessionTimeout() {

    }


}

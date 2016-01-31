package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Session;
import com.github.neunkasulle.chronocommand.model.User;
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

    /**
     * Gets the one Instance of the SessionControl.
     * @return The one SessionControl instance.
     */
    public static SessionControl getInstance() {
        return ourInstance;
    }

    /**
     * Deletes one user Session
     * @return true when the session could be killed
     */
    private boolean killSessions() {

        return false;
    }

    /**
     * Invoked by a timer to perform chronjob like actions
     */
    public void timeEvent() {
        throw new NotYetImplementedException();
    }

    /**
     * When the TTL of a Session is reched this method gets invoked
     */
    private void sessionTimeout() {
        throw new NotYetImplementedException();
    }

    /**
     * Returns the session of a specific user
     * @param user The user which session is needed
     * @return the session of this user
     */
    public Session getSession(User user) {
        throw new NotYetImplementedException();
    }


}

package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl extends Control {

    private static MainControl ourInstance = new MainControl();

    private MainControl() {
    }

    public static MainControl getInstance() {
        return ourInstance;
    }



    private void exceptionHandling() {
        throw new NotYetImplementedException();
    }

    private void startup() {
        //TODO link Realm with user DAO
        throw new NotYetImplementedException();
    }

    private void shutdown() {
        throw new NotYetImplementedException();
    }
}

package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl extends Control {

    private static MainControl ourInstance = new MainControl();

    private MainControl() {
        //TODO link Realm with user DAO
    }

    public static MainControl getInstance() {
        return ourInstance;
    }



    private void exceptionHandling() {
        throw new NotYetImplementedException();
    }

    private void startup() {
        throw new NotYetImplementedException();
    }

    private void shutdown() {
        throw new NotYetImplementedException();
    }
}

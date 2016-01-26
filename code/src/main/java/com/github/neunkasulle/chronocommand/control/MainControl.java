package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.hibernate.cfg.NotYetImplementedException;


/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl extends Control {


    private static MainControl ourInstance = new MainControl();

    private MainControl() {
        realm = new com.github.neunkasulle.chronocommand.security.Realm();
    }

    public static MainControl getInstance() {
        return ourInstance;
    }



    private void exceptionHandling() {
        throw new NotYetImplementedException();
    }

    public void startup() {
        throw new NotYetImplementedException();
    }

    public void shutdown() {
        throw new NotYetImplementedException();
    }
}

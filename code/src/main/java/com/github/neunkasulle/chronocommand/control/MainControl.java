package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.UserDAO;

import org.hibernate.cfg.NotYetImplementedException;


/**
 * Created by Janze on 18.01.2016.
 * Controling startup and shutdown
 */
public class MainControl extends Control {
    UserDAO userDAO;
    com.github.neunkasulle.chronocommand.security.Realm realm;

    private static MainControl ourInstance = new MainControl();

    private MainControl() {


        userDAO = new UserDAO();
        realm = new com.github.neunkasulle.chronocommand.security.Realm();

        realm.setUserDAO(userDAO);

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

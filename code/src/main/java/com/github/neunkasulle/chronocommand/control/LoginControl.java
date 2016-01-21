package com.github.neunkasulle.chronocommand.control;

import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * login handling
 */
public class LoginControl extends Control {

    private static LoginControl ourInstance = new LoginControl();

    private LoginControl() {

    }

    public static LoginControl getInstance() {
        return ourInstance;
    }



    public boolean login(String name, String hashedPw) {

        return false;
    }

    public void lostPassword() {
        throw new NotYetImplementedException();

    }

    public String hash(String password) {

        return null;
    }
}

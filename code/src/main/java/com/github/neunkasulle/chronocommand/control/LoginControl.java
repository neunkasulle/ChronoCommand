package com.github.neunkasulle.chronocommand.control;

/**
 * Created by Janze on 18.01.2016.
 */
public class LoginControl extends Control {


    private static LoginControl ourInstance = new LoginControl();

    public static LoginControl getInstance() {
        return ourInstance;
    }

    private LoginControl() {

    }

    public boolean login(String name, String hashedPw) {

        return false;
    }

    public void lostPassword() {

    }

    public String hash(String password) {

        return null;
    }
}

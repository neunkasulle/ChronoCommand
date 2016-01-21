package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.Supervisor;

/**
 * Created by Janze on 18.01.2016.
 */
public class CreateUserControl extends Control {


    private static CreateUserControl ourInstance = new CreateUserControl();

    public static CreateUserControl getInstance() {
        return ourInstance;
    }

    private CreateUserControl() {

    }

    public boolean createUser(Role userType, String name, String email, String password, Supervisor supervisor, int hoursPerMonth) {

        return false;
    }

    private void showErrorMessage(String errorCode) {

    }
}
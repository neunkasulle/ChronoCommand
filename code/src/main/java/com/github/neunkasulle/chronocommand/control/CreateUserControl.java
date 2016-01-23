package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;

/**
 * Created by Janze on 18.01.2016.
 * User creation Stuff
 */
public class CreateUserControl extends Control {
    private static CreateUserControl ourInstance = new CreateUserControl();
    
    private CreateUserControl() {

    }



    public static CreateUserControl getInstance() {
        return ourInstance;
    }


    public boolean createUser(Role userType, String name, String email, String password, User supervisor, int hoursPerMonth) {

        return false;
    }

    private void showErrorMessage(String errorCode) {

    }
}
package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Session;
import com.github.neunkasulle.chronocommand.model.User;

/**
 * Created by Janze on 18.01.2016.
 */
public class UserManagementControl extends  Control {

    private static UserManagementControl ourInstance = new UserManagementControl();

    public static UserManagementControl getInstance() {
        return ourInstance;
    }

    private UserManagementControl() {

    }

    public void addUser(User user) {

    }

    public void removeUser() {

    }

    public void editUser() {

    }

    public User getUser(Session session) {

        return null;
    }


}

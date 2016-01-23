package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.Session;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.apache.shiro.realm.Realm;
import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 18.01.2016.
 * Controls User management
 */
public class UserManagementControl extends  Control {

    private static UserManagementControl ourInstance = new UserManagementControl();

    private UserManagementControl() {

    }

    public static UserManagementControl getInstance() {
        return ourInstance;
    }



    public void addUser(User user) {
        throw new NotYetImplementedException();
    }

    public void removeUser() {
        throw new NotYetImplementedException();
    }

    public void editUser() {
        throw new NotYetImplementedException();
    }

    public User findUser(String username) {
        throw new NotYetImplementedException();
    }

    public User getUser(Session session) {

        return null;
    }


}

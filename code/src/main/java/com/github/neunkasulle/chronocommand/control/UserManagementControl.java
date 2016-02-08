package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.apache.shiro.SecurityUtils;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

/**
 * Created by Janze on 18.01.2016.
 * Controls User management
 */
public class UserManagementControl {

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

    public List<User> getUsersByRole(Role role) throws ChronoCommandException {
        User currentUser = LoginControl.getInstance().getCurrentUser();
        Role administrator = UserDAO.getInstance().getRoleByName(MainControl.ROLE_ADMINISTRATOR);
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        return UserDAO.getInstance().getUsersByRole(role);
    }

}

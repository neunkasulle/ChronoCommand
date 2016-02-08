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



    public void addUser(User user) throws ChronoCommandException {
        throw new NotYetImplementedException();
    }

    public void setUserDisabled(boolean disabled) throws ChronoCommandException {
        throw new NotYetImplementedException();
    }

    public void editUser(User user, String username, String realname, String email, String password) throws ChronoCommandException {
        if (SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR) || user.equals(LoginControl.getInstance().getCurrentUser())) {

            if (!username.isEmpty()) {
                user.setUsername(username);
            }
            if (!realname.isEmpty()) {
                user.setRealname(realname);
            }
            if (!email.isEmpty()) {
                user.setEmail(email);
            }
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
        } else {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
    }

    public User findUser(String username) throws ChronoCommandException {
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

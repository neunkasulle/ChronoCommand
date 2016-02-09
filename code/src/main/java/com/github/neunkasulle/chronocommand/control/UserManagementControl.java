package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.*;
import org.apache.shiro.SecurityUtils;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;
import java.util.Set;

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
            if (!username.isEmpty() && !user.getUsername().equals(username)) {
                if (UserDAO.getInstance().findUser(username) != null) {
                    throw new ChronoCommandException(Reason.USERNAMETAKEN);
                }
                user.setUsername(username);
            }
            if (!realname.isEmpty()) {
                user.setRealname(realname);
            }
            if (!email.isEmpty() && !user.getEmail().equals(email)) {
                if (UserDAO.getInstance().findUserByEmail(email) != null) {
                    throw new ChronoCommandException(Reason.EMAILALREADYINUSE);
                }
                user.setEmail(email);
            }
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
            UserDAO.getInstance().saveUser(user);
        } else {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
    }

    public User findUser(String username) throws ChronoCommandException {
        User user = UserDAO.getInstance().findUser(username);
        if (user == null) {
            throw new ChronoCommandException(Reason.NOSUCHUSER);
        }
        if (user.equals(LoginControl.getInstance().getCurrentUser())) {
            return user;
        }
    }

    public List<User> getUsersByRole(Role role) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        return UserDAO.getInstance().getUsersByRole(role);
    }

    public List<User> getUsersBySupervisor(User user) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_SUPERVISOR)) {
            throw new ChronoCommandException(Reason.INVALIDSOMETHING);
        }
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)
                && !(LoginControl.getInstance().getCurrentUser().equals(user))) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        return UserDAO.getInstance().getUsersBySupervisor(user);
    }

    public List<User> getAllUsers() throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        return UserDAO.getInstance().getAllUsers();
    }

    public Role getPrimaryRoleFromUser(User user) {
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.isPrimaryRole()) {
                return role;
            }
        }
        return null;
    }
}

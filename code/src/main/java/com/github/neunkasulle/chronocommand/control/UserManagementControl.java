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
            if (!username.isEmpty() && !user.getUsername().equals(username)) {
                if (UserDAO.getInstance().findUser(username) != null) {
                    throw new ChronoCommandException(Reason.USERNAMETAKEN);
                }
                user.setUsername(username);
            }

            user.setRealname(realname);

            if (UserDAO.getInstance().findUserByEmail(email) != null) {
                throw new ChronoCommandException(Reason.EMAILALREADYINUSE);
            }
            user.setEmail(email);

            user.setPassword(password);

            UserDAO.getInstance().saveUser(user);
        } else {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
    }

    public User findUser(String username) throws ChronoCommandException {
        if (!LoginControl.getInstance().isLoggedIn()) {
            throw new ChronoCommandException(Reason.NOTLOGGEDIN);
        }
        User user = UserDAO.getInstance().findUser(username);
        if (user == null) {
            throw new ChronoCommandException(Reason.NOSUCHUSER);
        }
        if (SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            return user;
        }
        if (user.equals(LoginControl.getInstance().getCurrentUser())) {
            return user;
        }
        if (LoginControl.getInstance().getCurrentUser().equals(user.getSupervisor())) {
            return user;
        }
        throw new ChronoCommandException(Reason.NOTPERMITTED);
    }

    public List<User> getUsersByRole(Role role) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        return UserDAO.getInstance().getUsersByRole(role);
    }

    public List<User> getUsersBySupervisor(User user) throws ChronoCommandException {
        if (!SecurityUtils.getSubject().isPermitted(Role.PERM_ADMINISTRATOR)
                && !(SecurityUtils.getSubject().isPermitted(Role.PERM_SUPERVISOR)
                    && LoginControl.getInstance().getCurrentUser().equals(user))) {
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

    public void createInitialAdministrator(String username, String email, String password, String realname) throws ChronoCommandException {
        if (!MainControl.getInstance().isInitialStartup()) {
            throw new ChronoCommandException(Reason.NOTPERMITTED);
        }
        Role admin = UserDAO.getInstance().getRoleByName(MainControl.ROLE_ADMINISTRATOR);
        User user = new User(admin, username.trim(), email.trim(), password, realname.trim(), null, 0);
        UserDAO.getInstance().saveUser(user);
    }

    public List<Role> getAllRoles() {
        return UserDAO.getInstance().getAllRoles();
    }

    public Role getRoleByName(String roleName) {
        return UserDAO.getInstance().getRoleByName(roleName);
    }
}

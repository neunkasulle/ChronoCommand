package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Reason;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Janze on 18.01.2016.
 * Handles Login and Logout
 */
public class LoginControl {
    private static LoginControl ourInstance = new LoginControl();
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginControl.class);

    private LoginControl() {
    }

    /**
     * Gets the one Instance of the LoginControl.
     * @return The one LogimControl instance.
     */
    public static LoginControl getInstance() {
        return ourInstance;
    }

    /**
     * Tries to log in a user with given credentials
     * @param username username which is trying to log in.
     * @param password password trying to authenticate the username
     * @param rememberMe when the session should be remembered this is true.
     * @throws ChronoCommandException if there are errors while trying to log in
     */
    public void login(String username, String password, boolean rememberMe) throws ChronoCommandException {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            LOGGER.error("Failed Login", e);
            throw new ChronoCommandException(Reason.BADCREDENTIALS);
        }
        LOGGER.info(username + " logged in successfully");

    }


    /**
     * logs out the an active user
     */
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * checks if user is logged in or remembered
     */
    public boolean isLoggedIn() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isRemembered() || subject.isAuthenticated();
    }

    public User getCurrentUser() throws ChronoCommandException {
        if (!isLoggedIn()) {
            LOGGER.error("user not logged in");
            throw new ChronoCommandException(Reason.NOTLOGGEDIN);
        }
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return UserDAO.getInstance().findUser(username);
    }
}

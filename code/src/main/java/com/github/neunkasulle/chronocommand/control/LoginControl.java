package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Reason;
import com.github.neunkasulle.chronocommand.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Janze on 18.01.2016.
 * Handles Login and Logout
 */
public class LoginControl extends Control {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(LoginControl.class);
    private static LoginControl ourInstance = new LoginControl();

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
            LOGGER.info("Failed Login", e);
            throw new ChronoCommandException(Reason.BADCREDENTIALS);
        }

    }

    /**
     * Sends a password reset mail when invoked
     */
    public void lostPassword() {
        throw new NotYetImplementedException();

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

}

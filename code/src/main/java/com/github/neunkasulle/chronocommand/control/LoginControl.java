package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.ChronoCommandException;
import com.github.neunkasulle.chronocommand.model.Reason;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Created by Janze on 18.01.2016.
 * login handling
 */
public class LoginControl extends Control {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(LoginControl.class);
    private static LoginControl ourInstance = new LoginControl();

    private LoginControl() {



    }

    public static LoginControl getInstance() {
        return ourInstance;
    }



    public void login(String username, String password, boolean rememberMe) throws ChronoCommandException {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            LOGGER.info("Failed Login");
            throw new ChronoCommandException(Reason.BADCREDENTIALS);
        }

    }

    public void lostPassword() {
        throw new NotYetImplementedException();

    }

    public void logout() {
        SecurityUtils.getSubject().logout();
    }

}

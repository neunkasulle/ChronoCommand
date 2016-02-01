package com.github.neunkasulle.chronocommand.control;

import com.github.neunkasulle.chronocommand.model.UserDAO;

/**
 * Created by Janze on 17.01.2016.
 * Abstract Class for controling program execution flow
 */
public abstract class Control {
    protected UserDAO userDAO;
    protected com.github.neunkasulle.chronocommand.security.Realm realm;

}

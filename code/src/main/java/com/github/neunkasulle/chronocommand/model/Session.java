package com.github.neunkasulle.chronocommand.model;

import java.util.Date;

/**
 * Created by Janze on 16.01.2016.
 */
public class Session {
    private int sessionID;
    private Date expire;
    private User sessionUser;

    public Session(User user, Date expire) {
        this.sessionUser = user;
        this.expire = expire;
    }
}

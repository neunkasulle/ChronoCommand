package com.github.neunkasulle.chronocommand.model;

/**
 * Created by Janze on 16.01.2016.
 */
public abstract class User {
    int id;
    String username;
    String email;
    String passwordhash;
    Role role;

    public User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        // TODO set password
    }

    public boolean checkPassword(String password)
    {
        return false;
    }
}

package com.github.neunkasulle.chronocommand.model;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.realm.Realm;

/**
 * Created by Janze on 16.01.2016.
 */
public abstract class User {
    int id;
    String username;
    String email;
    AuthenticationInfo authInfo;
    Role role;

    public User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        // TODO set password
    }

    public AuthenticationInfo getAuthInfo() {
        return authInfo;
    }


}

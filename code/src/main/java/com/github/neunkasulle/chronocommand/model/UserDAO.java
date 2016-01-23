package com.github.neunkasulle.chronocommand.model;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.realm.Realm;

/**
 * Created by jannis on 19.01.16.
 */
public class UserDAO{


    public User findUser(String username) {
        throw new UnsupportedOperationException();
    }

    public User getUser(Long userId) {
        //return (User) getSession().get(User.class, userId);
        throw new UnsupportedOperationException();
        //TODO FInd unser by ID
    }

    public boolean addUser(User newuser) {
        throw new UnsupportedOperationException();
    }

    public boolean deleteUser(User user) {
        throw new UnsupportedOperationException();
    }

    public Proletarier[] getProletarierBySupervisor(Supervisor supervisor) {
        throw new UnsupportedOperationException();
    }

    public Proletarier[] getAllProletarier() {
        throw new UnsupportedOperationException();
    }

    public boolean checkUserDetails(Role userRole, String name, String email,
                                    String password, Supervisor supervisor,
                                    int hoursPerMonth) {
        throw new UnsupportedOperationException();
    }


}

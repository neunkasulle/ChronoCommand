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
public class UserDAO implements Realm{
    String realmName = "AuthProv";

    public User findUser(String username) {
        throw new UnsupportedOperationException();
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


    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        User user = findUser(authenticationToken.getPrincipal().toString());

        if(user == null) {
            throw new AuthenticationException();
        }

        return user.getAuthInfo();
    }

    @Override
    public String getName() {
        return realmName;
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        CredentialsMatcher credentialsMatcher = new PasswordMatcher();

       User user = findUser(authenticationToken.getPrincipal().toString());

        if(user != null) {
            return credentialsMatcher.doCredentialsMatch(authenticationToken, user.getAuthInfo());
        }

        return false;

    }

}

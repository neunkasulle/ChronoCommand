package com.github.neunkasulle.chronocommand.security;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import com.github.neunkasulle.chronocommand.model.Role;
import com.github.neunkasulle.chronocommand.model.User;
import com.github.neunkasulle.chronocommand.model.UserDAO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * The Spring/Hibernate sample application's one and only configured Apache Shiro Realm.
 *
 * <p>Because a Realm is really just a security-specific DAO, we could have just made Hibernate calls directly
 * in the implementation and named it a 'HibernateRealm' or something similar.</p>
 *
 * <p>But we've decided to make the calls to the database using a UserDAO, since a DAO would be used in other areas
 * of a 'real' application in addition to here. We felt it better to use that same DAO to show code re-use.</p>
 */
public class Realm extends AuthorizingRealm {

    public Realm() {
        setName("SampleRealm"); //This name must match the name in the User class's getPrincipals() method
        setCredentialsMatcher(new HashedCredentialsMatcher("SHA-512"));
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        User user = UserDAO.getInstance().findUser(token.getUsername());
        if( user != null ) {
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
        } else {
            throw new AuthenticationException();
        }
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Long userId = (Long) principals.fromRealm(getName()).iterator().next();
        User user = UserDAO.getInstance().getUser(userId);
        if( user != null ) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for( Role role : user.getRoles() ) {
                info.addRole(role.getName());
                info.addStringPermissions( role.getPermissions() );
            }
            return info;
        } else {
            return null;
        }
    }

}




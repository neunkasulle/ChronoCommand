package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by Janze on 16.01.2016.
 */

@Entity
@Table(name="cc_users")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional=false)
    @Column(length=100)
    @org.hibernate.annotations.Index(name="idx_users_username")
    protected String username;

    @Basic(optional=false)
    @org.hibernate.annotations.Index(name="idx_users_email")
    protected String email;

    @Basic(optional=false)
    @Column(length=255)
    protected String password;

    @ManyToMany
    @JoinTable(name="cc_users_roles")
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    protected Set<Role> roles;

    @Basic(optional=false)
    protected boolean mailFlag;

    @Basic(optional=false)
    protected boolean isDisabled = false;

    @ManyToOne
    @JoinColumn(name="supervisor_id")
    User supervisor;


    public Long getId() {
        return id;
    }

    /**
     * Returns the username associated with this user account;
     *
     * @return the username associated with this user account;
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password for this user.
     *
     * @return this user's password
     */

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = this.roles;
    }

    public boolean isPermitted(String roleName) {
        for (Role role : roles) {
            if (role.getPermissions().contains(roleName)) {
                return true;
            }
        }

        return false;
    }

    public boolean getMailFlag() {
        return mailFlag;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisable(boolean disable) {
        isDisabled = disable;
    }

    public void setMailFlag(boolean mailFlag) {
        this.mailFlag = mailFlag;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User newSupervisor) {
        supervisor = newSupervisor;
    }
}

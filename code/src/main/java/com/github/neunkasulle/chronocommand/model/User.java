package com.github.neunkasulle.chronocommand.model;

import org.apache.shiro.crypto.hash.Sha512Hash;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.internal.util.compare.EqualsHelper;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Janze on 16.01.2016.
 * user object
 */

@Entity
@Table(name="cc_users")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class User {
    private final static int STRINGLENGTH = 254;

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional=false)
    @Column(length=STRINGLENGTH)
    @org.hibernate.annotations.Index(name="idx_users_username")
    protected String username;

    @Basic(optional=false)
    @Column(length=STRINGLENGTH)
    @org.hibernate.annotations.Index(name="idx_users_username")
    protected String realname;

    @Basic(optional=false)
    @Column(length = STRINGLENGTH)
    @org.hibernate.annotations.Index(name="idx_users_email")
    protected String email;

    @Basic(optional=false)
    @Column(length=511)
    protected Sha512Hash password;

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
    private User supervisor;

    @Basic
    private int hoursPerMonth;

    public User() {
        // hibernate needs this
    }

    public User(Role userType, String username, String email, String password, String realname, User supervisor,
                int hoursPerMonth) throws ChronoCommandException {
        this.roles = new HashSet<>();

        this.roles.add(userType);

        setUsername(username);

        setEmail(email);

        setPassword(password);

        setRealname(realname);

        setSupervisor(supervisor);

        setHoursPerMonth(hoursPerMonth);

    }

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

    public void setUsername(String username) throws ChronoCommandException {
        if (username == null || username.trim().isEmpty()) {
            throw new ChronoCommandException(Reason.INVALIDSTRING);
        }
        if(username.length() > STRINGLENGTH) {
            throw new ChronoCommandException(Reason.STRINGTOOLONG);
        }
        this.username = username;
    }

    /**
     * Returns the username associated with this user account;
     *
     * @return the username associated with this user account;
     */
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) throws ChronoCommandException {
        if (realname == null) {
            throw new ChronoCommandException(Reason.INVALIDSTRING);
        }
        realname = realname.trim();
        if (realname.isEmpty()) {
            throw new ChronoCommandException(Reason.INVALIDSTRING);
        }
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ChronoCommandException {
        if ( !email.contains("@") || !email.contains(".") ) {
            throw new ChronoCommandException(Reason.INVALIDEMAIL);
        }
        if(email.length() > STRINGLENGTH) {
            throw new ChronoCommandException(Reason.STRINGTOOLONG);
        }
        this.email = email;
    }

    /**
     * Returns the password for this user.
     *
     * @return this user's password
     */

    public Sha512Hash getPassword() {
        return password;
    }

    public void setPassword(String password) throws ChronoCommandException {
        if (password.isEmpty()) {
            throw new ChronoCommandException(Reason.INVALIDSTRING);
        }
        this.password = new Sha512Hash(password, null, 1024);
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public void setMailFlag(boolean mailFlag) {
        this.mailFlag = mailFlag;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisable(boolean disable) {
        isDisabled = disable;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User newSupervisor) {
        supervisor = newSupervisor;
    }

    public int getHoursPerMonth() {
        return hoursPerMonth;
    }

    public void setHoursPerMonth(int hoursPerMonth) throws ChronoCommandException {
        if (hoursPerMonth < 0 || hoursPerMonth > 80) {
            throw new  ChronoCommandException(Reason.INVALIDNUMBER);
        }
        this.hoursPerMonth = hoursPerMonth;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof User) && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    public Role getPrimaryRole() {
        for (Role role : roles) {
            if (role.isPrimaryRole()) {
                return role;
            }
        }
        return null;
    }
}

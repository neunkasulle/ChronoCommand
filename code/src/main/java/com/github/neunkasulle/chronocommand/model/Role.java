package com.github.neunkasulle.chronocommand.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by Janze on 16.01.2016.
 * User Roles
 */
@Entity
@Table(name="cc_roles")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Role {
    @Transient
    public static final String PERM_ADMINISTRATOR = "administrator";
    @Transient
    public static final String PERM_SUPERVISOR = "supervisor";
    @Transient
    public static final String PERM_PROLETARIER = "proletarier";
    @Transient
    public static final String PERM_LONGHOURS = "longhours";
    // TODO all the other permissions

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    @Column(length = 100)
    @Index(name = "idx_roles_name")
    private String name;

    @Basic//(optional = false)
    @Column(length = 255)
    private String description;

    @ElementCollection
    @JoinTable(name = "cc_roles_permissions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<String> permissions;

    @Basic(optional = false)
    private boolean primaryRole;

    protected Role() {
    }

    /**
     * Contructs a new role, without permissions
     * @param name the name of the new role
     */
    public Role(String name, boolean primaryRole) {
        this.name = name;
        this.primaryRole = primaryRole;
    }

    /**
     * Gets the internal DB id
     * @return the internal db id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the name of the role
     * @return the name as string
     */
    public String getName() {
        return name;
    }

    /**
     * Changes a role name
     * @param name the new name of the role
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the role
     * @return the description as string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a new description for that role
     * @param description A short description of what the role is
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the permissions of this role
     * @return the permissions of the role
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * Sets new permissions for this role
     * @param permissions the new permissions of the role, the old will be purged
     */
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * Whether this role is a primary role (administrator, supervisor or proletarier)
     */
    public boolean isPrimaryRole() {
        return primaryRole;
    }
}

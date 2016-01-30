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
 */
@Entity
@Table(name="cc_roles")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    @Column(length = 100)
    @Index(name = "idx_roles_name")
    private String name;

    @Basic(optional = false)
    @Column(length = 255)
    private String description;

    @ElementCollection
    @JoinTable(name = "cc_roles_permissions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<String> permissions;

    protected Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}

package com.training.entity;

import com.training.entity.enums.PermissionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue
    @Column(updatable = false)
    @ToString.Exclude
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true,
            nullable = false,
            updatable = false)
    private PermissionEnum name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public PermissionEnum getName() {
        return name;
    }

    public void setName(PermissionEnum name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) && name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

package com.annakirillova.crmsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Proxy(lazy = false)
public class User extends AbstractBaseEntity {

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;

    @Column(name = "username", nullable = false)
    @NotBlank
    private String username;

    @Column(name = "is_active", nullable = false, columnDefinition = "bool default true")
    private boolean isActive;

    public User(User user) {
        this(user.id, user.firstName, user.lastName, user.username, user.isActive);
    }

    public User(Integer id, String firstName, String lastName, String username, boolean isActive) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.isActive = isActive;
    }
}
package com.kirillova.gymcrmsystem.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password", callSuper = true)
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

    @Column(name = "password", nullable = false)
    @NotBlank
    protected String password;

    @Column(name = "is_active", nullable = false, columnDefinition = "bool default true")
    protected boolean isActive;

    public User(User user) {
        this(user.id, user.firstName, user.lastName, user.username, user.password, user.isActive);
    }

    public User(Integer id, String firstName, String lastName, String username, String password, boolean isActive) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }
}
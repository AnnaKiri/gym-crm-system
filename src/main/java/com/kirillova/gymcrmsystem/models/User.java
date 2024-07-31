package com.kirillova.gymcrmsystem.models;

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
@ToString(exclude = "password", callSuper = true)
@Proxy(lazy = false) // Отключаем использование прокси для этой сущности
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
    @Size(min = 5, max = 50)
    private String password;

    @Column(name = "is_active", nullable = false, columnDefinition = "bool default true")
    private boolean isActive;

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
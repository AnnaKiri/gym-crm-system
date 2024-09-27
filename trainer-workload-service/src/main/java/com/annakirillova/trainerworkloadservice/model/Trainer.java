package com.annakirillova.trainerworkloadservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Trainer")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Trainer extends AbstractBaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank
    private String username;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @Size(min = 1, max = 128)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    @Size(min = 1, max = 128)
    private String lastName;

    @Column(name = "is_active", nullable = false, columnDefinition = "bool default true")
    private boolean isActive;

    public Trainer(Integer id, String username, String firstName, String lastName, boolean isActive) {
        super(id);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }
}

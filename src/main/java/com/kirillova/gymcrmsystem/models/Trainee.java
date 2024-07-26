package com.kirillova.gymcrmsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "trainee")
@Getter
@Setter
@NoArgsConstructor
public class Trainee extends AbstractBaseEntity {

    @Column(name = "date_of_birth", nullable = false)
    @NotNull
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 5, max = 128)
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    public Trainee(Trainee trainee) {
        this(trainee.id, trainee.dateOfBirth, trainee.address, trainee.user);
    }

    public Trainee(Integer id, LocalDate dateOfBirth, String address, User user) {
        super(id);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", user=" + user.getId() +
                ", id=" + id +
                '}';
    }
}
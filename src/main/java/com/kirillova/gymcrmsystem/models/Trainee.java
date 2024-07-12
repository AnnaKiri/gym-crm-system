package com.kirillova.gymcrmsystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "trainee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Trainee {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "user_id")
    private long userId;

    public Trainee(Trainee trainee) {
        this(trainee.id, trainee.dateOfBirth, trainee.address, trainee.userId);
    }
}

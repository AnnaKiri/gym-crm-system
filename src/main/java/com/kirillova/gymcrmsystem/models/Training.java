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
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Training {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "trainee_id")
    private long traineeId;

    @Column(name = "trainer_id ")
    private long trainerId;

    @Column(name = "name")
    private String name;

    @Column(name = "type_id")
    private long typeId;

    @Column(name = "date")
    private Date date;

    @Column(name = "duration")
    private int duration;

    public Training(Training training) {
        this(training.id, training.traineeId, training.trainerId, training.name, training.typeId, training.date, training.duration);
    }
}

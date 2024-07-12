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


@Entity
@Table(name = "training_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainingType {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    public TrainingType(TrainingType trainingType) {
        this(trainingType.id, trainingType.name);
    }
}

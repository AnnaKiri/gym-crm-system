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
@Table(name = "training_type")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TrainingType extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 1, max = 128)
    private String name;

    public TrainingType(TrainingType trainingType) {
        this(trainingType.id, trainingType.name);
    }

    public TrainingType(Integer id, String name) {
        super(id);
        this.name = name;
    }
}

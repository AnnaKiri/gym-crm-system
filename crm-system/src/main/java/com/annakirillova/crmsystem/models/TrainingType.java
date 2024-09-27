package com.annakirillova.crmsystem.models;

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
@Table(name = "training_type")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
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

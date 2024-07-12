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
@Table(name = "trainer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Trainer {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "specialization_id")
    private long specializationId;

    @Column(name = "user_id")
    private long userId;

    public Trainer(Trainer trainer) {
        this(trainer.id, trainer.specializationId, trainer.userId);
    }
}

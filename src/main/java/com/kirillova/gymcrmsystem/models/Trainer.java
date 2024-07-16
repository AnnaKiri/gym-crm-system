package com.kirillova.gymcrmsystem.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "trainer")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Trainer extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", nullable = false)
    @NotNull
    private TrainingType specialization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    public Trainer(Trainer trainer) {
        this(trainer.id, trainer.specialization, trainer.user);
    }

    public Trainer(Integer id, TrainingType specialization, User user) {
        super(id);
        this.specialization = specialization;
        this.user = user;
    }
}

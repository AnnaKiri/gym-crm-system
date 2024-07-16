package com.kirillova.gymcrmsystem.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Training extends AbstractBaseEntity {

    @JoinColumn(name = "trainee_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Trainee trainee;

    @JoinColumn(name = "trainer_id ", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Trainer trainer;

    @Column(name = "name", nullable = false)
    @Size(min = 5, max = 128)
    @NotBlank
    private String name;

    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotBlank
    private TrainingType type;

    @Column(name = "date", nullable = false)
    @NotNull
    private Date date;

    @Column(name = "duration", nullable = false)
    @NotNull
    private int duration;

    public Training(Training training) {
        this(training.id, training.trainee, training.trainer, training.name, training.type, training.date, training.duration);
    }

    public Training(Integer id, Trainee trainee, Trainer trainer, String name, TrainingType type, Date date, int duration) {
        super(id);
        this.trainee = trainee;
        this.trainer = trainer;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }
}

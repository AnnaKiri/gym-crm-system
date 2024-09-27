package com.annakirillova.crmsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
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
    @Size(min = 2, max = 128)
    @NotBlank
    private String name;

    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private TrainingType type;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "duration", nullable = false)
    @NotNull
    private int duration;

    public Training(Training training) {
        this(training.id, training.trainee, training.trainer, training.name, training.type, training.date, training.duration);
    }

    public Training(Integer id, Trainee trainee, Trainer trainer, String name, TrainingType type, LocalDate date, int duration) {
        super(id);
        this.trainee = trainee;
        this.trainer = trainer;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainee=" + trainee.getId() +
                ", trainer=" + trainer.getId() +
                ", name='" + name + '\'' +
                ", type=" + type.getId() +
                ", date=" + date +
                ", duration=" + duration +
                ", id=" + id +
                '}';
    }
}

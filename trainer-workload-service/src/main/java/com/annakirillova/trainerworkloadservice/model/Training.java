package com.annakirillova.trainerworkloadservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "Training")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Training extends AbstractBaseEntity {

    @JoinColumn(name = "trainer_username", referencedColumnName = "username", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Trainer trainer;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "duration", nullable = false)
    @NotNull
    private int duration;

    public Training(Integer id, Trainer trainer, LocalDate date, int duration) {
        super(id);
        this.trainer = trainer;
        this.date = date;
        this.duration = duration;
    }
}

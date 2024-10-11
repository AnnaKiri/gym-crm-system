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

@Entity
@Table(name = "Summary")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Summary extends AbstractBaseEntity {

    @JoinColumn(name = "trainer_username", referencedColumnName = "username", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Trainer trainer;

    @Column(name = "training_year", nullable = false)
    @NotNull
    private Integer year;

    @Column(name = "training_month", nullable = false)
    @NotNull
    private Integer month;

    @Column(name = "duration", nullable = false)
    @NotNull
    private Integer duration;

    public Summary(Integer id, Trainer trainer, Integer year, Integer month, Integer duration) {
        super(id);
        this.trainer = trainer;
        this.year = year;
        this.month = month;
        this.duration = duration;
    }
}

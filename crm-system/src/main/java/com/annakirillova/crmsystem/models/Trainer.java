package com.annakirillova.crmsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "trainer")
@Getter
@Setter
@NoArgsConstructor
public class Trainer extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id", nullable = false)
    @NotNull
    private TrainingType specialization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ManyToMany(mappedBy = "trainerList", fetch = FetchType.LAZY)
    private List<Trainee> traineeList;

    public Trainer(Trainer trainer) {
        this(trainer.id, trainer.specialization, trainer.user);
    }

    public Trainer(Integer id, TrainingType specialization, User user) {
        super(id);
        this.specialization = specialization;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization.getId() +
                ", user=" + user.getId() +
                ", id=" + id +
                '}';
    }
}

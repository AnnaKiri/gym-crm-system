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
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "trainee")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Trainee extends AbstractBaseEntity {

    @Column(name = "date_of_birth", nullable = false)
    @NotNull
    private Date dateOfBirth;

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 5, max = 128)
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Trainer> trainerIdList;

    public Trainee(Trainee trainee) {
        this(trainee.id, trainee.dateOfBirth, trainee.address, trainee.user);
    }

    public Trainee(Integer id, Date dateOfBirth, String address, User user) {
        super(id);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }
}























package com.kirillova.gymcrmsystem.models;

import java.util.Objects;

public class Trainer {

    private long id;
    private long specializationId;
    private long userId;

    public Trainer() {
    }

    public Trainer(long id, long specializationId, long userId) {
        this.id = id;
        this.specializationId = specializationId;
        this.userId = userId;
    }

    public Trainer(Trainer trainer) {
        this(trainer.id, trainer.specializationId, trainer.userId);
    }

    public long getSpecializationId() {
        return specializationId;
    }

    public void setSpecialization(long specializationId) {
        this.specializationId = specializationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return id == trainer.id
                && specializationId == trainer.specializationId
                && userId == trainer.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, specializationId, userId);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specializationId=" + specializationId +
                ", userId=" + userId +
                ", id=" + id +
                '}';
    }
}

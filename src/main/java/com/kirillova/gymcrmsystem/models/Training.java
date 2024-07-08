package com.kirillova.gymcrmsystem.models;

import java.time.LocalDate;
import java.util.Objects;

public class Training {

    private long id;
    private long traineeId;
    private long trainerId;
    private String name;
    private long typeId;
    private LocalDate date;
    private int duration;

    public Training() {
    }

    public Training(long id, long traineeId, long trainerId, String name,
                    long typeId, LocalDate date, int duration) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.name = name;
        this.typeId = typeId;
        this.date = date;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(long traineeId) {
        this.traineeId = traineeId;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setLocalDate(LocalDate date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id
                && traineeId == training.traineeId
                && trainerId == training.trainerId
                && duration == training.duration
                && Objects.equals(name, training.name)
                && typeId == training.typeId
                && Objects.equals(date, training.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, traineeId, trainerId, name, typeId, date, duration);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                "traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", name='" + name + '\'' +
                ", typeId=" + typeId +
                ", date=" + date +
                ", duration=" + duration +
                '}';
    }
}

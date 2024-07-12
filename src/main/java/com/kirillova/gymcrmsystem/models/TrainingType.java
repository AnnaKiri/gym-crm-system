package com.kirillova.gymcrmsystem.models;

import java.util.Objects;

public class TrainingType {
    private long id;
    private String name;

    public TrainingType() {
    }

    public TrainingType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TrainingType(TrainingType trainingType) {
        this(trainingType.id, trainingType.name);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingType that = (TrainingType) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

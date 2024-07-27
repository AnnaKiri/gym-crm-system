package com.kirillova.gymcrmsystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

public interface Entity {
    Integer getId();

    void setId(Integer id);

    @JsonIgnore
    default boolean isNew() {
        return getId() == null;
    }

    default Integer id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}

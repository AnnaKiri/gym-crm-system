package com.kirillova.gymcrmsystem;

import org.springframework.util.Assert;

public interface HasId {
    Integer getId();

    void setId(Integer id);

    default boolean isNew() {
        return getId() == null;
    }

    default Integer id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}

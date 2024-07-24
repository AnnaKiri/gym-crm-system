package com.kirillova.gymcrmsystem.to;

import com.kirillova.gymcrmsystem.HasId;
import lombok.Data;

@Data
public abstract class BaseTo implements HasId {

    protected Integer id;

    public BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}

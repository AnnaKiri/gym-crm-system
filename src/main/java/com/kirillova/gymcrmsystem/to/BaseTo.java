package com.kirillova.gymcrmsystem.to;

import lombok.Data;
import com.kirillova.gymcrmsystem.HasId;

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

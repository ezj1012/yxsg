package com.yxbear.sg.engine.model;

import com.yxbear.sg.domain.SystemUtils;

import lombok.Data;

@Data
public class ProvinceLand {

    int id;

    Province province;

    public ProvinceLand(int id, Province province) {
        super();
        this.id = id;
        this.province = province;
    }

    public int toCityId() {
        return SystemUtils.wid2cid(id);
    }

}

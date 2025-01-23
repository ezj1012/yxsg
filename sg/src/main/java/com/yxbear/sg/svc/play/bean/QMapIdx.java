package com.yxbear.sg.svc.play.bean;

import com.yxbear.sg.domain.SystemUtils;

import lombok.Data;

@Data
public class QMapIdx {

    private int x;

    private int y;

    private int yw = 2;

    private int xw = 2;

    public long getIdx() {
        return SystemUtils.wid(x, y);
    }

}

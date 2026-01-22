package com.yxbear.core.coder.bean;

import lombok.Data;

@Data
public class GraphNodeCfg {

    String id;

    long x;

    long y;

    int width;

    // int height; // 自动计算

    int zIndex;

}

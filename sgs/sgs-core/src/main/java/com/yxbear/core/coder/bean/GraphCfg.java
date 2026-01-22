package com.yxbear.core.coder.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GraphCfg {

    String name;

    List<GraphNodeCfg> nodes = new ArrayList<>();

}

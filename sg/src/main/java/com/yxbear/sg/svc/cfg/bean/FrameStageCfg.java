package com.yxbear.sg.svc.cfg.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FrameStageCfg {

    String key;

    String name;

    List<String> comps = new ArrayList<>();

}

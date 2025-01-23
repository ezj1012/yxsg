package com.yxbear.sg.svc.cfg.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FrameCfg {

    String rsmHttpRoot = "/rsm/images/";

    List<String> imgs = new ArrayList<>();

    List<FrameStageCfg> stages = new ArrayList<>();

}

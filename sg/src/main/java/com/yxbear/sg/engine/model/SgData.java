package com.yxbear.sg.engine.model;

import java.util.List;

import lombok.Data;

@Data
public class SgData {

    List<Province> provinces;

    String playerTemplate;

    String cityTemplate;

    int jobBuild = 3;

    int jobTech = 3;
}

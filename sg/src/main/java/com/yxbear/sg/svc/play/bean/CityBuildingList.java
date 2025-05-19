package com.yxbear.sg.svc.play.bean;

import lombok.Data;

import java.util.List;

@Data
public class CityBuildingList {
    private int cid;
    private List<CityBuildingUseRes> buildings;
}

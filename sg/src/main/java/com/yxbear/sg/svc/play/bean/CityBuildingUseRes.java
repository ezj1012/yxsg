package com.yxbear.sg.svc.play.bean;

import com.yxbear.sg.domain.model.cfg.CfgBuilding;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.svc.cfg.bean.Building;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class CityBuildingUseRes {
    int id;
    int lv;
    int place;
    UseRes res;

    public CityBuildingUseRes(Building b, int lv, UseRes res) {
        this.id = b.getId();
        place = b.getPlace();
        this.lv = lv;
        this.res = res;
    }

}

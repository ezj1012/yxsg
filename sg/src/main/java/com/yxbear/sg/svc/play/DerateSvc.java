package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiCityHero;
import com.yxbear.sg.svc.play.bean.UseRes;

public interface DerateSvc {

    UseRes derateRes(GiCity city, GiCityHero hero, CfgBuildingLevel globLv, UseRes res);
}
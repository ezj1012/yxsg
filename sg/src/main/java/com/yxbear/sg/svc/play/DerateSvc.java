package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.svc.play.bean.UseRes;

public interface DerateSvc {

    UseRes derateRes(int playId, Long heroAffairs, CfgBuildingLevel goalLv, UseRes res);

}
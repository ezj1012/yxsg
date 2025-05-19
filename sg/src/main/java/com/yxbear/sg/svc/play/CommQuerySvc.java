package com.yxbear.sg.svc.play;

import java.util.Collection;
import java.util.List;

import com.yxbear.sg.domain.model.cfg.CfgGoods;
import com.yxbear.sg.svc.play.bean.CityBuildingList;
import com.yxbear.sg.svc.play.bean.CityInfo;

public interface CommQuerySvc {

    CityInfo getCityInfo(int cid);

    List<CfgGoods> getGoods(Collection<Integer> ids);

    CityBuildingList getCityBuildUpgradeInfo(int cid, Boolean inner);
}
package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import com.yxbear.sg.engine.model.ProvinceLand;

public interface CitySvc {

    void createFirstCity(ProvinceLand land, Userable user);

    void createBuilding(int cid, int pos, int bId);

    void upgradeBuilding(int cid, int cbId);

    // void degradeBuilding(int cid, int cbId, boolean useGoods);

    void deleteAll(int playId);

    void finishBuildUpgrading(MemCityBuildUpgrading item);
}

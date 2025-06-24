package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import com.yxbear.sg.engine.model.ProvinceLand;

public interface CitySvc {

    void createFirstCity(ProvinceLand land, Userable user);

    void createBuilding(int cid, int pos, int bId);

    void upgradeBuilding(int cid, int cbId);

    void deleteAll(int playId);

    void finishBuildUpgrading(MemCityBuildUpgrading item);

    /**
     * 更新城池资源生产
     * <li>建筑升降级</li>
     * <li>科技升降</li>
     * <li>城守装备变更</li>
     * <li>生产比例变化</li>
     * 
     * @param cid
     */
    void updateCityProduction(int cid);

    /**
     * 更新城池资源
     * 
     * @param cid
     */

    void updateCityResource(int cid);
}

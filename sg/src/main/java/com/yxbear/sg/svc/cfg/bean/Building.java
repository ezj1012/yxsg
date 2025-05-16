package com.yxbear.sg.svc.cfg.bean;

import java.util.HashMap;
import java.util.Map;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.model.cfg.CfgBuilding;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Building extends CfgBuilding {
    Map<Integer, CfgBuildingLevel> levels;
    Map<Integer, Map<Integer, Map<Integer, Integer>>> preCdts;

    public boolean hasLv(int lv) {
        return this.levels.containsKey(lv);
    }

    public CfgBuildingLevel getAndCheckLv(int lv) {
        if (!this.levels.containsKey(lv)) {
            throw new ServiceException("建筑[" + this.getName() + "]等级[" + lv + "]不存!");
        }
        return this.levels.get(lv);
    }

    /**
     * 前提条件
     *
     * @param lv id
     * @return 0, 建筑要求 1,科技要求 2,物品要求
     */
    public Map<Integer, Map<Integer, Integer>> getPreCdts(int lv) {
        Map<Integer, Map<Integer, Integer>> r = this.preCdts.get(lv);
        return r == null ? new HashMap<>() : r;
    }

}

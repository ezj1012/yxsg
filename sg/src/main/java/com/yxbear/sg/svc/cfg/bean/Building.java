package com.yxbear.sg.svc.cfg.bean;

import java.util.List;
import java.util.Map;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.model.cfg.CfgBuilding;
import com.yxbear.sg.domain.model.cfg.CfgBuildingCondition;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Building extends CfgBuilding {
    Map<Integer, CfgBuildingLevel> levels;
    Map<Integer, List<CfgBuildingCondition>> preCdts;

    public boolean hasLv(int lv) {
        return this.levels.containsKey(lv);
    }

    public CfgBuildingLevel getAndCheckLv(int lv) {
        if (!this.levels.containsKey(lv)) {
            throw new ServiceException("建筑[" + this.getName() + "]等级[" + lv + "]不存!");
        }
        return this.levels.get(lv);
    }

    public List<CfgBuildingCondition> getPreCdts(int lv) {
        return this.preCdts.get(lv);
    }
}

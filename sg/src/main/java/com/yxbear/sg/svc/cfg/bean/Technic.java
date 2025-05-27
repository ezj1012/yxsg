package com.yxbear.sg.svc.cfg.bean;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.model.cfg.CfgTechnic;
import com.yxbear.sg.domain.model.cfg.CfgTechnicLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class Technic extends CfgTechnic {
    Map<Integer, CfgTechnicLevel> levels;
    Map<Integer, Map<Integer, Map<Integer, Integer>>> preCdts;

    public boolean hasLv(int lv) {
        return this.levels.containsKey(lv);
    }

    public CfgTechnicLevel getAndCheckLv(int lv) {
        if (!this.levels.containsKey(lv)) {
            throw new ServiceException("科技[" + this.getName() + "]等级[" + lv + "]不存!");
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

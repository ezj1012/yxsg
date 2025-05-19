package com.yxbear.sg.svc.play.impl;

import com.yxbear.sg.domain.model.gi.GiCityHero;
import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.engine.PlayContext;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.svc.play.DerateSvc;
import com.yxbear.sg.svc.play.bean.UseRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DerateSvcImpl implements DerateSvc {
    final SgEngine sgEngine;

    static final int[] baseResDerateFid = {
            60, // 0-5
            61,// 0-12
            62,// 0-?
    };
    static final int baseResDerateRate = 30; // 减免百分比
    /**
     * 建筑技术 每升1级，加快建筑、城防工事的建造速度10%。
     */
    static final int buildTechId = 46;//
    static final int buildTechDerateRate = 30;
    static final double heroBuildDerateRate = 0.5;


    @Override
    public UseRes derateRes(int playId, Long heroAffairs, CfgBuildingLevel goalLv, UseRes res) {
        PlayContext ctx = sgEngine.getPlayCtx(playId);

        boolean hasBaseResDerate;
        if (goalLv.getLevel() <= 5) {
            hasBaseResDerate = ctx.hasUsingGoods(baseResDerateFid);
        } else if (goalLv.getLevel() <= 12) {
            hasBaseResDerate = ctx.hasUsingGoods(baseResDerateFid[1], baseResDerateFid[2]);
        } else {
            hasBaseResDerate = ctx.hasUsingGoods(baseResDerateFid[2]);
        }

        if (hasBaseResDerate) {
            res.derateBaseRes(baseResDerateRate);
        }
        if (heroAffairs != null && heroAffairs != 0) {
            res.derateTime(heroAffairs, heroBuildDerateRate);
        }
        res.derateTime(ctx.getTechLv(buildTechId), buildTechDerateRate);
        return res;
    }

}

package com.yxbear.sg.svc.play.bean;

import java.util.HashMap;
import java.util.Map;

import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;

import lombok.Data;

@Data
public class UseRes {

    /**
     * 消耗木材
     */
    private long upgradeWood;

    /**
     * 消耗石头
     */
    private long upgradeRock;

    /**
     * 消耗铁定
     */
    private long upgradeIron;

    /**
     * 消耗粮食
     */
    private long upgradeFood;

    /**
     * 升级消耗金币
     */
    private long upgradeGold;

    /**
     * 升级依赖人口
     */
    private int upgradePeople;

    /**
     * 升级耗时
     */
    private long upgradeTime;

    /**
     * 占用人口
     */
    private long usingPeople;

    private Map<Integer, Integer> useGoods = new HashMap<>();

    public void derateBaseRes(int rate) {
        upgradeFood = SystemUtils.calculateDerateAndMin1(upgradeFood, 1L, rate);
        upgradeWood = SystemUtils.calculateDerateAndMin1(upgradeWood, 1L, rate);
        upgradeRock = SystemUtils.calculateDerateAndMin1(upgradeRock, 1L, rate);
        upgradeIron = SystemUtils.calculateDerateAndMin1(upgradeIron, 1L, rate);
        upgradeGold = SystemUtils.calculateDerateAndMin1(upgradeGold, 1L, rate);
    }

    public void derateTime(int lv, int rate) {
        upgradeTime = SystemUtils.calculateDerateAndMin1(upgradeTime, (long) lv, rate);
    }

    public void derateTime(long attr, double rate) {
        upgradeTime = SystemUtils.calculateDerateAndMin1(upgradeTime, attr, rate);
    }

    public static UseRes from(CfgBuildingLevel lv) {
        return from(lv, null);
    }

    public static UseRes from(CfgBuildingLevel lv, Map<Integer, Integer> useGoods) {
        UseRes useRes = SystemUtils.copy(lv, UseRes.class);

        if (useGoods != null && !useGoods.isEmpty()) {
            useRes.getUseGoods().putAll(useGoods);
        }

        return useRes;
    }

    public Long getUpgradeEndTimeMillis() {
        return System.currentTimeMillis() + upgradeTime * 1000;
    }
}

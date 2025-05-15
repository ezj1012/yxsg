package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CCfgBuildingLevel implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer buildId;

    private Integer[] buildIds;

    private Integer startBuildId;

    private Integer endBuildId;

    private Integer level;

    private Integer[] levels;

    private Integer startLevel;

    private Integer endLevel;

    private Long upgradeWood;

    private Long[] upgradeWoods;

    private Long startUpgradeWood;

    private Long endUpgradeWood;

    private Long upgradeRock;

    private Long[] upgradeRocks;

    private Long startUpgradeRock;

    private Long endUpgradeRock;

    private Long upgradeIron;

    private Long[] upgradeIrons;

    private Long startUpgradeIron;

    private Long endUpgradeIron;

    private Long upgradeFood;

    private Long[] upgradeFoods;

    private Long startUpgradeFood;

    private Long endUpgradeFood;

    private Long upgradeGold;

    private Long[] upgradeGolds;

    private Long startUpgradeGold;

    private Long endUpgradeGold;

    private Integer upgradePeople;

    private Integer[] upgradePeoples;

    private Integer startUpgradePeople;

    private Integer endUpgradePeople;

    private Long upgradeTime;

    private Long[] upgradeTimes;

    private Long startUpgradeTime;

    private Long endUpgradeTime;

    private Long usingPeople;

    private Long[] usingPeoples;

    private Long startUsingPeople;

    private Long endUsingPeople;

    private String description;

    private String[] descriptions;

    private String descriptionEqual;

    private Long createTime;

    private Long[] createTimes;

    private Long startCreateTime;

    private Long endCreateTime;

    private Long modifyTime;

    private Long[] modifyTimes;

    private Long startModifyTime;

    private Long endModifyTime;

}
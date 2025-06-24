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
public class CCfgSoldier implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer soldierType;

    private Integer[] soldierTypes;

    private Integer startSoldierType;

    private Integer endSoldierType;

    private Integer fromCity;

    private Integer[] fromCitys;

    private Integer startFromCity;

    private Integer endFromCity;

    private String name;

    private String[] names;

    private String nameEqual;

    private String description;

    private String[] descriptions;

    private String descriptionEqual;

    private Long hp;

    private Long[] hps;

    private Long startHp;

    private Long endHp;

    private Long ap;

    private Long[] aps;

    private Long startAp;

    private Long endAp;

    private Long dp;

    private Long[] dps;

    private Long startDp;

    private Long endDp;

    private Long apRange;

    private Long[] apRanges;

    private Long startApRange;

    private Long endApRange;

    private Long speed;

    private Long[] speeds;

    private Long startSpeed;

    private Long endSpeed;

    private Long carry;

    private Long[] carrys;

    private Long startCarry;

    private Long endCarry;

    private Long timeNeed;

    private Long[] timeNeeds;

    private Long startTimeNeed;

    private Long endTimeNeed;

    private Long woodNeed;

    private Long[] woodNeeds;

    private Long startWoodNeed;

    private Long endWoodNeed;

    private Long rockNeed;

    private Long[] rockNeeds;

    private Long startRockNeed;

    private Long endRockNeed;

    private Long ironNeed;

    private Long[] ironNeeds;

    private Long startIronNeed;

    private Long endIronNeed;

    private Long foodNeed;

    private Long[] foodNeeds;

    private Long startFoodNeed;

    private Long endFoodNeed;

    private Long goldNeed;

    private Long[] goldNeeds;

    private Long startGoldNeed;

    private Long endGoldNeed;

    private Long peopleNeed;

    private Long[] peopleNeeds;

    private Long startPeopleNeed;

    private Long endPeopleNeed;

    private Long foodUse;

    private Long[] foodUses;

    private Long startFoodUse;

    private Long endFoodUse;

}
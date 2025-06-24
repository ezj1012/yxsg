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
public class CCfgDefence implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer defenceType;

    private Integer[] defenceTypes;

    private Integer startDefenceType;

    private Integer endDefenceType;

    private String name;

    private String[] names;

    private String nameEqual;

    private String description;

    private String[] descriptions;

    private String descriptionEqual;

    private Integer hp;

    private Integer[] hps;

    private Integer startHp;

    private Integer endHp;

    private Integer ap;

    private Integer[] aps;

    private Integer startAp;

    private Integer endAp;

    private Integer dp;

    private Integer[] dps;

    private Integer startDp;

    private Integer endDp;

    private Integer apRange;

    private Integer[] apRanges;

    private Integer startApRange;

    private Integer endApRange;

    private Integer speed;

    private Integer[] speeds;

    private Integer startSpeed;

    private Integer endSpeed;

    private Integer carry;

    private Integer[] carrys;

    private Integer startCarry;

    private Integer endCarry;

    private Integer timeNeed;

    private Integer[] timeNeeds;

    private Integer startTimeNeed;

    private Integer endTimeNeed;

    private Integer woodNeed;

    private Integer[] woodNeeds;

    private Integer startWoodNeed;

    private Integer endWoodNeed;

    private Integer rockNeed;

    private Integer[] rockNeeds;

    private Integer startRockNeed;

    private Integer endRockNeed;

    private Integer ironNeed;

    private Integer[] ironNeeds;

    private Integer startIronNeed;

    private Integer endIronNeed;

    private Integer foodNeed;

    private Integer[] foodNeeds;

    private Integer startFoodNeed;

    private Integer endFoodNeed;

    private Integer goldNeed;

    private Integer[] goldNeeds;

    private Integer startGoldNeed;

    private Integer endGoldNeed;

    private Integer areaNeed;

    private Integer[] areaNeeds;

    private Integer startAreaNeed;

    private Integer endAreaNeed;

}
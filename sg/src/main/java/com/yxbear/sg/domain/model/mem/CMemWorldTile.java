package com.yxbear.sg.domain.model.mem;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CMemWorldTile implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer tileType;

    private Integer[] tileTypes;

    private Integer startTileType;

    private Integer endTileType;

    private Integer province;

    private Integer[] provinces;

    private Integer startProvince;

    private Integer endProvince;

    private Integer jun;

    private Integer[] juns;

    private Integer startJun;

    private Integer endJun;

    private Integer curLevel;

    private Integer[] curLevels;

    private Integer startCurLevel;

    private Integer endCurLevel;

    private Integer maxLevel;

    private Integer[] maxLevels;

    private Integer startMaxLevel;

    private Integer endMaxLevel;

    private Long ownerCityId;

    private Long[] ownerCityIds;

    private Long startOwnerCityId;

    private Long endOwnerCityId;

    private Integer state;

    private Integer[] states;

    private Integer startState;

    private Integer endState;

}
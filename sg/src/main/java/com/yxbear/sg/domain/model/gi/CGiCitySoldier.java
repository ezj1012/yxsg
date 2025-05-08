package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGiCitySoldier implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer cityId;

    private Integer[] cityIds;

    private Integer startCityId;

    private Integer endCityId;

    private Integer soldierId;

    private Integer[] soldierIds;

    private Integer startSoldierId;

    private Integer endSoldierId;

    private Long count;

    private Long[] counts;

    private Long startCount;

    private Long endCount;

    private Integer curLevel;

    private Integer[] curLevels;

    private Integer startCurLevel;

    private Integer endCurLevel;

}
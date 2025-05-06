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
public class CGiCityDefence implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer cityId;

    private Integer[] cityIds;

    private Integer startCityId;

    private Integer endCityId;

    private Integer defenceId;

    private Integer[] defenceIds;

    private Integer startDefenceId;

    private Integer endDefenceId;

    private Long count;

    private Long[] counts;

    private Long startCount;

    private Long endCount;

}
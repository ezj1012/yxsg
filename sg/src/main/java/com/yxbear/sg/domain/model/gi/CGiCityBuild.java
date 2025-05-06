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
public class CGiCityBuild implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer cityId;

    private Integer[] cityIds;

    private Integer startCityId;

    private Integer endCityId;

    private Integer bid;

    private Integer[] bids;

    private Integer startBid;

    private Integer endBid;

    private Integer pos;

    private Integer[] poss;

    private Integer startPos;

    private Integer endPos;

    private Integer lv;

    private Integer[] lvs;

    private Integer startLv;

    private Integer endLv;

    private Integer status;

    private Integer[] statuss;

    private Integer startStatus;

    private Integer endStatus;

}
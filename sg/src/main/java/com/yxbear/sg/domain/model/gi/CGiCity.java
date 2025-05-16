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
public class CGiCity implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer playId;

    private Integer[] playIds;

    private Integer startPlayId;

    private Integer endPlayId;

    private String name;

    private String[] names;

    private String nameEqual;

    private Integer cityType;

    private Integer[] cityTypes;

    private Integer startCityType;

    private Integer endCityType;

    private Integer state;

    private Integer[] states;

    private Integer startState;

    private Integer endState;

    private Integer chiefhId;

    private Integer[] chiefhIds;

    private Integer startChiefhId;

    private Integer endChiefhId;

    private Integer counsellorId;

    private Integer[] counsellorIds;

    private Integer startCounsellorId;

    private Integer endCounsellorId;

    private Integer generalId;

    private Integer[] generalIds;

    private Integer startGeneralId;

    private Integer endGeneralId;

    private Integer collegelv;

    private Integer[] collegelvs;

    private Integer startCollegelv;

    private Integer endCollegelv;

    private Integer provinceId;

    private Integer[] provinceIds;

    private Integer startProvinceId;

    private Integer endProvinceId;

    private Integer conscript;

    private Integer[] conscripts;

    private Integer startConscript;

    private Integer endConscript;

    private Long discardTime;

    private Long[] discardTimes;

    private Long startDiscardTime;

    private Long endDiscardTime;

}
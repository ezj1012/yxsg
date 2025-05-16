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
public class CMemTechnicUpgrading implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer playId;

    private Integer[] playIds;

    private Integer startPlayId;

    private Integer endPlayId;

    private Integer cityId;

    private Integer[] cityIds;

    private Integer startCityId;

    private Integer endCityId;

    private Integer technicId;

    private Integer[] technicIds;

    private Integer startTechnicId;

    private Integer endTechnicId;

    private Long startTime;

    private Long[] startTimes;

    private Long startStartTime;

    private Long endStartTime;

    private Long endTime;

    private Long[] endTimes;

    private Long startEndTime;

    private Long endEndTime;

}
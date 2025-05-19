package com.yxbear.sg.svc.play.bean;

import com.yxbear.sg.domain.model.gi.GiCityBuild;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityBuilding extends GiCityBuild {

    /**
     * 目标等级
     */
    private Integer goalLv;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 下一个等级
     */
    private Integer nextLv = -1;

}

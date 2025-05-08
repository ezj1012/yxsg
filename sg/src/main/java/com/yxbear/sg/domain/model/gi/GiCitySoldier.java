package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCitySoldier implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 城池ID */
    private Integer cityId;

    /** 士兵模版ID */
    private Integer soldierId;

    /** 数量 */
    private Long count;

    /** 等级 */
    private Integer curLevel;

}
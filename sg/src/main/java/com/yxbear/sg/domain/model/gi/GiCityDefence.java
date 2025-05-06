package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCityDefence implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 玩家ID */
    private Integer cityId;

    /** 城防ID */
    private Integer defenceId;

    /** 数量 */
    private Long count;

}
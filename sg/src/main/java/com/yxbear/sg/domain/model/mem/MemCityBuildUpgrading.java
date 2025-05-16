package com.yxbear.sg.domain.model.mem;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemCityBuildUpgrading implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private Integer playId;

    /**  */
    private Integer cityId;

    /** :升级中,2:降级 */
    private Integer status;

    /** 目标等级 */
    private Integer goalLv;

    /** 结束时间 */
    private Long endTime;

}
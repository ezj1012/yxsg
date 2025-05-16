package com.yxbear.sg.domain.model.mem;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemTechnicUpgrading implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private Integer playId;

    /**  */
    private Integer cityId;

    /**  */
    private Integer technicId;

    /** 开始时间 */
    private Long startTime;

    /** 结束时间 */
    private Long endTime;

}
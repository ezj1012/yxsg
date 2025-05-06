package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCityBuild implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 城池ID */
    private Integer cityId;

    /** 建筑ID */
    private Integer bid;

    /** 建筑位置 */
    private Integer pos;

    /** 建筑等级 */
    private Integer lv;

    /** 0:正常,1:升级中,2:降级 */
    private Integer status;

}
package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class CfgBuildingCondition implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 建筑ID */
    private Integer buildId;

    /** 建筑等级 */
    private Integer levelId;

    /** 升级条件类型: 0,建筑要求 1,科技要求 2,物品要求 */
    private Integer preType;

    /** 类型对应ID */
    private Integer preId;

    /** 对应等级 */
    private Integer preLevel;

}
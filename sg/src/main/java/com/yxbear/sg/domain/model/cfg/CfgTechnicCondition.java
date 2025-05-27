package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgTechnicCondition implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private Integer technicId;

    /** 目标等级 */
    private Integer levelId;

    /** 升级条件类型: 0,建筑要求 1,科技要求,2 物品 */
    private Integer preType;

    /** 类型对应ID */
    private Integer preId;

    /** 对应等级|数量 */
    private Integer preLevel;

}
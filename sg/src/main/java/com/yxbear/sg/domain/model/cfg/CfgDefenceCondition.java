package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgDefenceCondition implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private Integer defenceId;

    /** 0 建筑 1 科技 */
    private Integer preType;

    /**  */
    private Integer preId;

    /**  */
    private Integer preLevel;

}
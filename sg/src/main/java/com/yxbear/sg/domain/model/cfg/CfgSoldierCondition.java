package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgSoldierCondition implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private Integer soldierId;

    /**  */
    private Integer preType;

    /**  */
    private Integer preId;

    /**  */
    private Integer preLevel;

}
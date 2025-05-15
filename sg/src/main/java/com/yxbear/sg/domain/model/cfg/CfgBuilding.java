package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class CfgBuilding implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 中文名 */
    private String name;

    /** 英文名 */
    private String typeName;

    /**  */
    private String description;

    /** 0: 外 1:内 */
    private Integer place;

}
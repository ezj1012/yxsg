package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgTechnic implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 中文名 */
    private String name;

    /** 英文名 */
    private String typeName;

    /**  */
    private String description;

}
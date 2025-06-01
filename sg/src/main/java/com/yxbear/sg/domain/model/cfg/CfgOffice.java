package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgOffice implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 名字 */
    private String name;

    /** 职位薪水 */
    private Long salary;

}
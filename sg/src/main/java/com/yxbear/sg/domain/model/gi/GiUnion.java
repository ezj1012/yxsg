package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiUnion implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 名字 */
    private String name;

}
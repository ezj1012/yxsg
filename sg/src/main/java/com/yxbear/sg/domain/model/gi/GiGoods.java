package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiGoods implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 物品ID */
    private Integer goodsId;

    /** 数量 */
    private Integer count;

}
package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGiGoods implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer goodsId;

    private Integer[] goodsIds;

    private Integer startGoodsId;

    private Integer endGoodsId;

    private Integer count;

    private Integer[] counts;

    private Integer startCount;

    private Integer endCount;

}
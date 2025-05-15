package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiGoods;
import com.yxbear.sg.domain.model.gi.GiGoods;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiGoodsMapper extends BaseMapper<Integer, GiGoods, CGiGoods> {

}
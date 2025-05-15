package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgGoods;
import com.yxbear.sg.domain.model.cfg.CfgGoods;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgGoodsMapper extends BaseMapper<Integer, CfgGoods, CCfgGoods> {

}
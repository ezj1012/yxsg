package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgSoldier;
import com.yxbear.sg.domain.model.cfg.CfgSoldier;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgSoldierMapper extends BaseMapper<Integer, CfgSoldier, CCfgSoldier> {

}
package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgBuilding;
import com.yxbear.sg.domain.model.cfg.CfgBuilding;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgBuildingMapper extends BaseMapper<Integer, CfgBuilding, CCfgBuilding> {

}
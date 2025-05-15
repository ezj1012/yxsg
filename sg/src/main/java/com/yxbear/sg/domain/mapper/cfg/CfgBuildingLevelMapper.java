package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgBuildingLevel;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgBuildingLevelMapper extends BaseMapper<Integer, CfgBuildingLevel, CCfgBuildingLevel> {

}
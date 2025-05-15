package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgBuildingCondition;
import com.yxbear.sg.domain.model.cfg.CfgBuildingCondition;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgBuildingConditionMapper extends BaseMapper<Integer, CfgBuildingCondition, CCfgBuildingCondition> {

}
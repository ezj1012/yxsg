package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgSoldierCondition;
import com.yxbear.sg.domain.model.cfg.CfgSoldierCondition;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgSoldierConditionMapper extends BaseMapper<Integer, CfgSoldierCondition, CCfgSoldierCondition> {

}
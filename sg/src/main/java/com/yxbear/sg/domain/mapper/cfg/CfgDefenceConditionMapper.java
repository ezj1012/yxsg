package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgDefenceCondition;
import com.yxbear.sg.domain.model.cfg.CfgDefenceCondition;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgDefenceConditionMapper extends BaseMapper<Integer, CfgDefenceCondition, CCfgDefenceCondition> {

}
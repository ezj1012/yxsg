package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgTechnicCondition;
import com.yxbear.sg.domain.model.cfg.CfgTechnicCondition;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgTechnicConditionMapper extends BaseMapper<Integer, CfgTechnicCondition, CCfgTechnicCondition> {

}
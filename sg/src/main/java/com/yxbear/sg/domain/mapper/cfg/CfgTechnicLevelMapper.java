package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgTechnicLevel;
import com.yxbear.sg.domain.model.cfg.CfgTechnicLevel;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgTechnicLevelMapper extends BaseMapper<Integer, CfgTechnicLevel, CCfgTechnicLevel> {

}
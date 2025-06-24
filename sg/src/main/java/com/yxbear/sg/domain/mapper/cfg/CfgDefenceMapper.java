package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgDefence;
import com.yxbear.sg.domain.model.cfg.CfgDefence;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgDefenceMapper extends BaseMapper<Integer, CfgDefence, CCfgDefence> {

}
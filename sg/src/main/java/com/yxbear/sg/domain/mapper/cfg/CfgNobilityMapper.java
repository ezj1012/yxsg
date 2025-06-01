package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgNobility;
import com.yxbear.sg.domain.model.cfg.CfgNobility;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgNobilityMapper extends BaseMapper<Integer, CfgNobility, CCfgNobility> {

}
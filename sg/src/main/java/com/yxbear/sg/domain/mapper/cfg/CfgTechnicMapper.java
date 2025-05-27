package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgTechnic;
import com.yxbear.sg.domain.model.cfg.CfgTechnic;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgTechnicMapper extends BaseMapper<Integer, CfgTechnic, CCfgTechnic> {

}
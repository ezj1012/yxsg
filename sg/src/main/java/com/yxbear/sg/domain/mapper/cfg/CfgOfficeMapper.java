package com.yxbear.sg.domain.mapper.cfg;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.cfg.CCfgOffice;
import com.yxbear.sg.domain.model.cfg.CfgOffice;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface CfgOfficeMapper extends BaseMapper<Integer, CfgOffice, CCfgOffice> {

}
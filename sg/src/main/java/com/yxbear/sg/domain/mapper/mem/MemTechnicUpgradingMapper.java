package com.yxbear.sg.domain.mapper.mem;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.mem.CMemTechnicUpgrading;
import com.yxbear.sg.domain.model.mem.MemTechnicUpgrading;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface MemTechnicUpgradingMapper extends BaseMapper<Integer, MemTechnicUpgrading, CMemTechnicUpgrading> {

}
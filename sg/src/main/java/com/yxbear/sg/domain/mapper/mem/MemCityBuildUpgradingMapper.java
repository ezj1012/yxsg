package com.yxbear.sg.domain.mapper.mem;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.mem.CMemCityBuildUpgrading;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface MemCityBuildUpgradingMapper extends BaseMapper<Integer, MemCityBuildUpgrading, CMemCityBuildUpgrading> {

}
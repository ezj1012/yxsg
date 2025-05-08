package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCitySoldier;
import com.yxbear.sg.domain.model.gi.GiCitySoldier;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCitySoldierMapper extends BaseMapper<Integer, GiCitySoldier, CGiCitySoldier> {

}
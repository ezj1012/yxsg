package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCityDefence;
import com.yxbear.sg.domain.model.gi.GiCityDefence;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityDefenceMapper extends BaseMapper<Integer, GiCityDefence, CGiCityDefence> {

}
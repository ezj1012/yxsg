package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCityBuild;
import com.yxbear.sg.domain.model.gi.GiCityBuild;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityBuildMapper extends BaseMapper<Integer, GiCityBuild, CGiCityBuild> {

}
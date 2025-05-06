package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCity;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityMapper extends BaseMapper<Integer, GiCity, CGiCity> {

}
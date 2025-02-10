package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCityResourceAdd;
import com.yxbear.sg.domain.model.gi.GiCityResourceAdd;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityResourceAddMapper extends BaseMapper<Integer, GiCityResourceAdd, CGiCityResourceAdd> {

}
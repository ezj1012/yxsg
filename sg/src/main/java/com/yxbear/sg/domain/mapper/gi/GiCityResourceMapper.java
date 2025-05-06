package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCityResource;
import com.yxbear.sg.domain.model.gi.GiCityResource;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityResourceMapper extends BaseMapper<Integer, GiCityResource, CGiCityResource> {

}
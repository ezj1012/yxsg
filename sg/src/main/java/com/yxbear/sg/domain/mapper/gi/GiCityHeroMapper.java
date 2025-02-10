package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiCityHero;
import com.yxbear.sg.domain.model.gi.GiCityHero;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityHeroMapper extends BaseMapper<Integer, GiCityHero, CGiCityHero> {

}
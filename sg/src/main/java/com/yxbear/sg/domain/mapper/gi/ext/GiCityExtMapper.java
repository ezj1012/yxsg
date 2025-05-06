package com.yxbear.sg.domain.mapper.gi.ext;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.yxbear.sg.domain.model.gi.CGiCity;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityExtMapper extends BaseMapper<Integer, GiCity, CGiCity> {

}
package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiUnion;
import com.yxbear.sg.domain.model.gi.GiUnion;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiUnionMapper extends BaseMapper<Integer, GiUnion, CGiUnion> {

}
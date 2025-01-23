package com.yxbear.sg.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.gi.CGiPlayer;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiPlayerMapper extends BaseMapper<Integer, GiPlayer, CGiPlayer> {

}
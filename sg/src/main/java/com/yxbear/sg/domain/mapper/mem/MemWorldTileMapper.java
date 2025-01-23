package com.yxbear.sg.domain.mapper.mem;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.mem.CMemWorldTile;
import com.yxbear.sg.domain.model.mem.MemWorldTile;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface MemWorldTileMapper extends BaseMapper<Integer, MemWorldTile, CMemWorldTile> {

}
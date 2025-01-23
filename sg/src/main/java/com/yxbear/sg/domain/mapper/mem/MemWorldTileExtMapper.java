package com.yxbear.sg.domain.mapper.mem;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.yxbear.sg.domain.model.mem.MemWorldTile;

@Mapper
public interface MemWorldTileExtMapper {
    // SELECT ID, PROVINCE
    // FROM (
    // SELECT
    // ID,
    // PROVINCE,
    // ROW_NUMBER() OVER (PARTITION BY PROVINCE ORDER BY RAND()) AS rn
    // FROM MEM_WORLD_TILE
    // WHERE TILE_TYPE = 1 -- 如果不需要根据TILE_TYPE过滤，请移除此条件
    // ) AS subquery
    // WHERE rn <= 10 order by province

    @Select("SELECT ID, PROVINCE FROM ("//
            + "SELECT ID, PROVINCE, ROW_NUMBER() OVER (PARTITION BY PROVINCE ORDER BY RAND()) "//
            + "AS rn FROM MEM_WORLD_TILE WHERE TILE_TYPE = 1 AND OWNER_CITY_ID = 0 AND PROVINCE < 14 AND state = 0" //
            + ") AS subquery WHERE rn <= 10")
    List<MemWorldTile> loadAvailableLands();
    
    @Select({
        "<script>",
        "SELECT ID",
        "FROM MEM_WORLD_TILE",
        "WHERE PROVINCE = #{provinceId}",
        "AND OWNER_CITY_ID = 0",
        "AND STATE = 0",
        "<if test='lockedIds != null and !lockedIds.isEmpty()'>",
        "AND ID NOT IN",
        "<foreach item='id' index='index' collection='lockedIds' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</if>",
        "ORDER BY RAND()",
        "LIMIT 10",
        "</script>"
    })
    List<Integer> loadAvailableLandsByProvinceId(int provinceId, List<Integer> lockedIds);

}

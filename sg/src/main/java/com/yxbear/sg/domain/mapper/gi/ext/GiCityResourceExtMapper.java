package com.yxbear.sg.domain.mapper.gi.ext;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GiCityResourceExtMapper {

    @Update({
            "UPDATE GI_CITY_RESOURCE SET",
            "WOOD = WOOD - #{upgradeWood},",
            "ROCK = ROCK - #{upgradeRock},",
            "IRON = IRON - #{upgradeIron},",
            "FOOD = FOOD - #{upgradeFood},",
            "GOLD = GOLD - #{upgradeGold},",
            "PEOPLE = PEOPLE - #{upgradePeople}",
            "WHERE ID = #{cid}"
    })
    void useBaseRes(int cid,
                   long upgradeWood,
                   long upgradeRock,
                   long upgradeIron,
                   long upgradeFood,
                   long upgradeGold,
                   int upgradePeople);

}

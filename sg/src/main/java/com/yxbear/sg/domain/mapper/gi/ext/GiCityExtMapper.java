package com.yxbear.sg.domain.mapper.gi.ext;

import com.yxbear.sg.domain.model.gi.GiCityHero;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.yxbear.sg.domain.model.gi.GiCity;

@Mapper
public interface GiCityExtMapper {

    @Select("SELECT ID, chiefh_Id,counsellor_Id,general_Id FROM GI_CITY WHERE ID = #{cid}")
    GiCity getCityTakeOfficeHero(int cid);

    @Select("select ch.affairs_Add_On   FROM GI_CITY_HERO ch where id in ( select c.chiefh_id  from GI_CITY c where c.id =#{cid} )")
    Long getCityChiefHeroAffairs(int cid);
}
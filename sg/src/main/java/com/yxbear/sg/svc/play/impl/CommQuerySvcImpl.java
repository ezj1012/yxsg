package com.yxbear.sg.svc.play.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yxbear.core.CommUtils;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.mapper.cfg.CfgGoodsMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityBuildMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityDefenceMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityResourceAddMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityResourceMapper;
import com.yxbear.sg.domain.mapper.gi.GiCitySoldierMapper;
import com.yxbear.sg.domain.model.cfg.CCfgGoods;
import com.yxbear.sg.domain.model.cfg.CfgGoods;
import com.yxbear.sg.domain.model.gi.CGiCityBuild;
import com.yxbear.sg.domain.model.gi.CGiCityDefence;
import com.yxbear.sg.domain.model.gi.CGiCitySoldier;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.svc.play.CommQuerySvc;
import com.yxbear.sg.svc.play.bean.CityBuilding;
import com.yxbear.sg.svc.play.bean.CityInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommQuerySvcImpl implements CommQuerySvc {

    final GiCityMapper cityMapper;
    final GiCityResourceMapper resMapper;
    final GiCityResourceAddMapper resAddMapper;
    final GiCityBuildMapper buildMapper;
    final GiCityDefenceMapper defMapper;
    final GiCitySoldierMapper soldierMapper;

    final CfgGoodsMapper goodsMapper;

    @Override
    public CityInfo getCityInfo(int cid) {
        GiCity city = cityMapper.selectById(cid);
        if (city == null) {
            return null;
        }
        CityInfo info = SystemUtils.copy(city, CityInfo.class);
        info.setRes(resMapper.selectById(cid));
        info.setResAdd(resAddMapper.selectById(cid));
        info.getBuildings().addAll(
                buildMapper.queryList(CGiCityBuild.builder().cityId(cid).build(), "pos").stream()
                        .map(b -> SystemUtils.copy(b, CityBuilding.class)).toList());
        info.getSoldiers().addAll(
                soldierMapper.queryList(CGiCitySoldier.builder().cityId(cid).build(), "id"));
        info.getDefences().addAll(
                defMapper.queryList(CGiCityDefence.builder().cityId(cid).build(), "id"));
        int lv = info.getBuildings().get(0).getLv();
        info.setMaxOuterBuild(20 + lv * 3);
        return info;
    }

    @Override
    public List<CfgGoods> getGoods(Collection<Integer> ids) {
        if (CommUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return goodsMapper.queryList(CCfgGoods.builder().ids(ids.toArray(Integer[]::new)).build(), "id");
    }

}

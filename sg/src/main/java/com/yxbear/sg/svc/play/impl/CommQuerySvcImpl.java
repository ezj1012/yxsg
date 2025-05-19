package com.yxbear.sg.svc.play.impl;

import java.util.*;

import com.yxbear.sg.domain.SGConstant;
import com.yxbear.sg.domain.mapper.gi.*;
import com.yxbear.sg.domain.mapper.gi.ext.GiCityExtMapper;
import com.yxbear.sg.domain.mapper.mem.MemCityBuildUpgradingMapper;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.domain.model.gi.*;
import com.yxbear.sg.domain.model.mem.CMemCityBuildUpgrading;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.play.DerateSvc;
import com.yxbear.sg.svc.play.bean.*;
import org.springframework.stereotype.Service;

import com.yxbear.core.CommUtils;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.mapper.cfg.CfgGoodsMapper;
import com.yxbear.sg.domain.model.cfg.CCfgGoods;
import com.yxbear.sg.domain.model.cfg.CfgGoods;
import com.yxbear.sg.svc.play.CommQuerySvc;

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
    final GiCityHeroMapper heroMapper;
    private final GiCityExtMapper giCityExtMapper;

    final MemCityBuildUpgradingMapper mbMapper;

    final FrameCfgSvc cfgSvc;
    final CfgGoodsMapper goodsMapper;
    final DerateSvc derateSvc;


    @Override
    public CityInfo getCityInfo(int cid) {
        GiCity city = cityMapper.selectById(cid);
        if (city == null) {
            return null;
        }
        CityInfo info = SystemUtils.copy(city, CityInfo.class);

        info.setHeros(heroMapper.queryList(CGiCityHero.builder().cityId(cid).build(), "name"));

        info.setRes(resMapper.selectById(cid));
        info.setResAdd(resAddMapper.selectById(cid));
        info.getBuildings().addAll(
                buildMapper.queryList(CGiCityBuild.builder().cityId(cid).build(), "pos").stream()
                        .map(b -> SystemUtils.copy(b, CityBuilding.class)).toList());
        Integer[] bids = info.getBuildings().stream().filter(b -> b.getStatus() != 0).map(GiCityBuild::getId).toArray(Integer[]::new);

        if (bids.length != 0) {
            Map<Integer, MemCityBuildUpgrading> idMap = new HashMap<>();
            mbMapper.queryList(CMemCityBuildUpgrading.builder().ids(bids).build(), "id")
                    .forEach(b -> idMap.put(b.getId(), b));
            info.getBuildings().forEach(b -> {
                MemCityBuildUpgrading memCityBuildUpgrading = idMap.get(b.getId());
                if (memCityBuildUpgrading != null) {
                    b.setGoalLv(memCityBuildUpgrading.getGoalLv());
                    b.setEndTime(memCityBuildUpgrading.getEndTime());
                }
            });
        }

        info.getSoldiers().addAll(
                soldierMapper.queryList(CGiCitySoldier.builder().cityId(cid).build(), "id"));
        info.getDefences().addAll(
                defMapper.queryList(CGiCityDefence.builder().cityId(cid).build(), "id"));
        int lv = info.getBuildings().getFirst().getLv();
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

    @Override
    public CityBuildingList getCityBuildUpgradeInfo(int cid, Boolean inner) {
        GiCity city = cityMapper.selectById(cid);
        SystemUtils.checkEmpty(city, "城池");
        final int playId = city.getPlayId();
        Long heroAffairs = giCityExtMapper.getCityChiefHeroAffairs(cid);


        List<CityBuildingUseRes> res = new ArrayList<>();
        cfgSvc.getGlobalCfg().getBuildingsMap().forEach((bid, b) -> {
            if (inner == null || inner && b.getPlace() == 1 || !inner && b.getPlace() == 0) {
                CfgBuildingLevel goalLv = b.getAndCheckLv(1);
                UseRes useRes = derateSvc.derateRes(playId, heroAffairs, goalLv, UseRes.from(goalLv));
                CityBuildingUseRes cityBuildingUseRes = new CityBuildingUseRes(b, goalLv.getLevel(), useRes);
                res.add(cityBuildingUseRes);
            }
        });
        res.sort(Comparator.comparingInt(CityBuildingUseRes::getId));

        CityBuildingList result = new CityBuildingList();
        result.setBuildings(res);

        return result;
    }


}

package com.yxbear.sg.svc.play.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.yxbear.core.CommUtils;
import com.yxbear.sg.domain.mapper.gi.ext.GiCityResourceExtMapper;
import com.yxbear.sg.domain.mapper.mem.MemCityBuildUpgradingMapper;
import com.yxbear.sg.domain.model.gi.*;
import com.yxbear.sg.domain.model.mem.CMemCityBuildUpgrading;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.mapper.gi.GiCityBuildMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityDefenceMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityHeroMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityResourceAddMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityResourceMapper;
import com.yxbear.sg.domain.model.cfg.CfgBuildingCondition;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.engine.PlayContext;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.cfg.bean.Building;
import com.yxbear.sg.svc.cfg.bean.CityCreator;
import com.yxbear.sg.svc.egimpl.ModelFactory;
import com.yxbear.sg.svc.play.CitySvc;
import com.yxbear.sg.svc.play.DerateSvc;
import com.yxbear.sg.svc.play.PlayStateSvc;
import com.yxbear.sg.svc.play.bean.PlayInfo;
import com.yxbear.sg.svc.play.bean.UseRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitySvcImpl implements CitySvc {

    /**
     * 特殊建筑ID集合：这些ID代表游戏中具有唯一功能的建筑类型。
     * <pre>
     * 编号对应建筑如下：
     * 6  - 官府
     * 7  - 书院
     * 13 - 市场
     * 14 - 铁匠铺
     * 16 - 马厩
     * 15 - 工匠作坊
     * 10 - 客栈
     * 17 - 仓库
     * 8  - 校场
     * 11 - 招贤馆
     * 12 - 鸿胪寺
     * 18 - 驿站
     * 19 - 烽火台
     * 20 - 城墙
     * </pre>
     */
    public static final Set<Integer> UNIQUE_BUILD_IDS = Set.of(6, 7, 13, 14, 16, 15, 10, 17, 8, 11, 12, 18, 19, 20);

    final SgEngine sgEngine;
    final FrameCfgSvc cfgSvc;
    final PlayStateSvc playStateSvc;
    final ModelFactory modelFactory;
    final GiCityMapper cityMapper;
    final GiCityResourceMapper cityResourceMapper;
    final GiCityResourceExtMapper cityResourceExtMapper;


    final GiCityResourceAddMapper cityResourceAddMapper;
    final GiCityDefenceMapper cityDefenceMapper;
    final GiCityBuildMapper cityBuildMapper;
    final GiCityHeroMapper heroMapper;

    final MemCityBuildUpgradingMapper mbMapper;

    final DerateSvc derateSvc;

    @Override
    public void createFirstCity(ProvinceLand land, Userable user) {
        CityCreator cityCreator = modelFactory.withFirstCity(land, user);
        cityMapper.save(cityCreator.getCity());
        cityResourceMapper.save(cityCreator.getResource());
        cityResourceAddMapper.save(cityCreator.getResourceAdd());
        cityCreator.getBuilds().forEach(cityBuildMapper::save);
        cityCreator.getDefences().forEach(cityDefenceMapper::save);
    }

    public GiCity createLastCity(PlayInfo info) {
        ProvinceLand land = sgEngine.getWorldMgr().randomAndLockLand(0);
        try {
            createFirstCity(land, SystemUtils.getCurUser());
            return cityMapper.selectById(land.toCityId());
        } finally {
            sgEngine.getWorldMgr().release(land);
        }
    }

    @Override
    public void deleteAll(int playId) {
        List<GiCity> citys = cityMapper.queryList(CGiCity.builder().playId(playId).build(), null);
        if (citys.isEmpty()) {
            return;
        }
        Integer[] cityIds = citys.stream().map(GiCity::getId).toArray(Integer[]::new);

        cityResourceMapper.deleteByCdt(CGiCityResource.builder().ids(cityIds).build());
        cityResourceAddMapper.deleteByCdt(CGiCityResourceAdd.builder().ids(cityIds).build());
        cityDefenceMapper.deleteByCdt(CGiCityDefence.builder().cityIds(cityIds).build());
        cityBuildMapper.deleteByCdt(CGiCityBuild.builder().cityIds(cityIds).build());
        cityMapper.deleteByCdt(CGiCity.builder().ids(cityIds).build());
    }

    @Override
    public void upgradeBuilding(int cid, int cbId) {
        GiCityBuild build = cityBuildMapper.selectById(cbId);
        SystemUtils.check(build == null, "城池建筑不存在!");
        SystemUtils.check(build.getStatus() != 0, build.getStatus() == 1 ? "建筑升级中!" : "建筑拆除中!");

        Building building = cfgSvc.getGlobalCfg().getAndCheckBuilding(build.getBid());
        GiCity city = getAndCheck(cid);
        PlayContext playCtx = sgEngine.getPlayCtx(city.getPlayId());
        int lv = build.getLv() + 1;
        UseRes useRes = checkAllAndDeductRes(city, building, lv, playCtx);

        GiCityBuild b = new GiCityBuild();
        b.setId(cbId);
        b.setStatus(1);
        cityBuildMapper.updateById(cbId, b);

        MemCityBuildUpgrading mb = new MemCityBuildUpgrading(cbId, city.getPlayId(), cid, 1, lv, useRes.getUpgradeEndTimeMillis());
        mbMapper.save(mb);
        sgEngine.event(mb);
    }

    @Transactional
    @Override
    public void createBuilding(int cid, int pos, int bId) {
        int lv = 1;
        Building building = cfgSvc.getGlobalCfg().getAndCheckBuilding(bId);
        GiCity city = getAndCheck(cid);
        PlayContext playCtx = sgEngine.getPlayCtx(city.getPlayId());

        SystemUtils.check(!checkPosAndCount(cid, pos), "城池空间不足!");
        SystemUtils.check(!checkUnique(cid, bId), "建筑[" + building.getName() + "]已存在!");

        UseRes useRes = checkAllAndDeductRes(city, building, lv, playCtx);

        GiCityBuild b = new GiCityBuild();
        b.setCityId(cid);
        b.setBid(bId);
        b.setPos(pos);
        b.setLv(1);
        b.setStatus(1);
        cityBuildMapper.save(b);
        int cbId = b.getId();

        MemCityBuildUpgrading mb = new MemCityBuildUpgrading(cbId, city.getPlayId(), cid, 1, lv, useRes.getUpgradeEndTimeMillis());
        mbMapper.save(mb);
        sgEngine.event(mb);
    }


    private UseRes checkAllAndDeductRes(GiCity city, Building building, int lv, PlayContext playCtx) {
        SystemUtils.check(!playCtx.checkBuildingCount(countWorkProcess(city.getId())), "队列已经满!", 1000);
        CfgBuildingLevel level = building.getAndCheckLv(lv);
        SystemUtils.check(level == null, "建筑已经达到最高等级!");
        // 0,建筑要求 1,科技要求 2,物品要求
        Map<Integer, Map<Integer, Integer>> preCdts = building.getPreCdts(lv);

        GiCityResource res = cityResourceMapper.selectById(city.getId());
        GiCityHero hero = city.getChiefhId() == null ? null : heroMapper.selectById(city.getChiefhId());

        UseRes useRes = UseRes.from(level, preCdts.get(2));
        useRes = derateSvc.derateRes(city, hero, level, useRes);
        SystemUtils.check(!check(res, useRes), "城池资源不足!");
        SystemUtils.check(!checkBuildCdt(city.getId(), preCdts.get(0)), "建筑要求不满足!");
        SystemUtils.check(!playCtx.checkTech(city.getCollegelv(), preCdts.get(1)), "所需科技不满足!");
        SystemUtils.check(!playCtx.checkGoods(useRes.getUseGoods()), "所需物品不满足!");

        // 扣除资源
        useBaseRes(city.getId(), useRes);
        playCtx.use(useRes.getUseGoods());

        return useRes;
    }


    private int countWorkProcess(int cid) {
        return cityBuildMapper.count(CGiCityBuild.builder().cityId(cid).startStatus(0).build()).intValue();
    }

    public void useBaseRes(int cid, UseRes res) {
        cityResourceExtMapper.useBaseRes(cid, res.getUpgradeWood()
                , res.getUpgradeRock()
                , res.getUpgradeIron()
                , res.getUpgradeFood()
                , res.getUpgradeGold()
                , res.getUpgradePeople()
        );
    }

    public boolean checkBuildCdt(int cityId, Map<Integer, Integer> buildIdAndLv) {
        if (CommUtils.isEmpty(buildIdAndLv)) return true;
        Map<Integer, Integer> idLv = cityBuildMapper.queryList(CGiCityBuild.builder().cityId(cityId).bids(buildIdAndLv.keySet().toArray(Integer[]::new)).build(), "id")
                .stream().collect(Collectors.toMap(GiCityBuild::getBid, GiCityBuild::getLv));
        for (Map.Entry<Integer, Integer> e : buildIdAndLv.entrySet()) {
            Integer rLv = idLv.get(e.getKey());
            if (rLv == null || rLv < e.getValue()) return false;
        }
        return true;
    }

    public boolean check(GiCityResource res, UseRes useRes) {
        return res.getFood() >= useRes.getUpgradeFood() && //
                res.getWood() >= useRes.getUpgradeWood() && //
                res.getRock() >= useRes.getUpgradeRock() && //
                res.getIron() >= useRes.getUpgradeIron() && //
                res.getGold() >= useRes.getUpgradeGold() && //
                res.getPeople() >= useRes.getUpgradePeople();
    }

    private GiCity getAndCheck(int cid) {
        GiCity city = cityMapper.selectById(cid);
        if (city == null) {
            throw new ServiceException("城池不存在!");
        }
        return city;
    }

    private boolean checkPosAndCount(int cityId, int pos) {
        if (pos >= 0 && pos < 34 || pos >= 100 && pos <= 196) {
            Long count = cityBuildMapper.count(CGiCityBuild.builder().cityId(cityId).pos(pos).build());
            if (count < 1) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUnique(int cityId, int bid) {
        if (UNIQUE_BUILD_IDS.contains(bid)) {
            Long count = cityBuildMapper.count(CGiCityBuild.builder().cityId(cityId).bid(bid).build());
            return count <= 0;
        }
        return true;
    }

    @Transactional
    @Override
    public void finishBuildUpgrading(List<MemCityBuildUpgrading> list) {

    }
}

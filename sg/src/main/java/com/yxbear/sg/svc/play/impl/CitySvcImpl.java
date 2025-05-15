package com.yxbear.sg.svc.play.impl;

import java.util.List;
import java.util.Map;

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
import com.yxbear.sg.domain.model.gi.CGiCity;
import com.yxbear.sg.domain.model.gi.CGiCityBuild;
import com.yxbear.sg.domain.model.gi.CGiCityDefence;
import com.yxbear.sg.domain.model.gi.CGiCityResource;
import com.yxbear.sg.domain.model.gi.CGiCityResourceAdd;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiCityHero;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.cfg.bean.Building;
import com.yxbear.sg.svc.cfg.bean.CityCreator;
import com.yxbear.sg.svc.egimpl.ModelFactory;
import com.yxbear.sg.svc.play.CitySvc;
import com.yxbear.sg.svc.play.bean.PlayInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitySvcImpl implements CitySvc {

    final SgEngine sgEngine;
    final FrameCfgSvc cfgSvc;
    final ModelFactory modelFactory;

    final GiCityMapper cityMapper;

    final GiCityResourceMapper cityResourceMapper;

    final GiCityResourceAddMapper cityResourceAddMapper;

    final GiCityDefenceMapper cityDefenceMapper;

    final GiCityBuildMapper cityBuildMapper;
    final GiCityHeroMapper heroMapper;

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

    @Transactional
    @Override
    public void createBuilding(int cid, int pos, int bId) {
        int lv = 1;

        checkPos(pos);
        Building building = cfgSvc.getGlobalCfg().getAndCheckBuilding(bId);
        CfgBuildingLevel level = building.getAndCheckLv(lv);
        List<CfgBuildingCondition> preCdts = building.getPreCdts(lv);

        GiCity city = getAndCheck(cid);
        GiCityHero hero = city.getChiefhId() == null ? null : heroMapper.selectById(city.getChiefhId());
        checkRes(cid, level, preCdts);
        useRes(cid, hero, level, preCdts);
        // addJob();
    }

    private MemCityBuild build() {
        
    }

    private GiCity getAndCheck(int cid) {
        GiCity city = cityMapper.selectById(cid);
        if (city == null) {
            throw new ServiceException("城池不存在!");
        }
        return city;
    }

    private void checkPos(int pos) {
        if (pos >= 0 && pos < 34 || pos >= 100 && pos <= 196) {
            return;
        }
        throw new ServiceException("城池空间不足!");
    }

    private void checkRes(int cid, CfgBuildingLevel level, List<CfgBuildingCondition> cdts) {
    }

    private void useRes(int cid, GiCityHero hero, CfgBuildingLevel level, List<CfgBuildingCondition> cdts) {

    }
}

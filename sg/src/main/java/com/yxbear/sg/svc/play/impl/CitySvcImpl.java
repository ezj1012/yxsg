package com.yxbear.sg.svc.play.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.mapper.gi.GiCityBuildMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityDefenceMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityResourceAddMapper;
import com.yxbear.sg.domain.mapper.gi.GiCityResourceMapper;
import com.yxbear.sg.domain.model.gi.CGiCity;
import com.yxbear.sg.domain.model.gi.CGiCityBuild;
import com.yxbear.sg.domain.model.gi.CGiCityDefence;
import com.yxbear.sg.domain.model.gi.CGiCityResource;
import com.yxbear.sg.domain.model.gi.CGiCityResourceAdd;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.cfg.bean.CityCreator;
import com.yxbear.sg.svc.egimpl.ModelFactory;
import com.yxbear.sg.svc.play.CitySvc;
import com.yxbear.sg.svc.play.bean.PlayInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitySvcImpl implements CitySvc {

    final SgEngine sgEngine;

    final ModelFactory modelFactory;

    final GiCityMapper cityMapper;

    final GiCityResourceMapper cityResourceMapper;

    final GiCityResourceAddMapper cityResourceAddMapper;

    final GiCityDefenceMapper cityDefenceMapper;

    final GiCityBuildMapper cityBuildMapper;

    @Override
    public void createFirstCity(ProvinceLand land, Userable user) {
        CityCreator cityCreator = modelFactory.withFirstCity(land, user);
        cityMapper.save(cityCreator.getCity());
        // cityResourceMapper.save(cityCreator.getResource());
        // cityResourceAddMapper.save(cityCreator.getResourceAdd());
        // cityCreator.getDefences().forEach(cityDefenceMapper::save);
        // cityCreator.getBuilds().forEach(cityBuildMapper::save);
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
        if (citys.isEmpty()) { return; }
        Integer[] cityIds = citys.stream().map(GiCity::getId).toArray(Integer[]::new);

        cityResourceMapper.deleteByCdt(CGiCityResource.builder().ids(cityIds).build());
        cityResourceAddMapper.deleteByCdt(CGiCityResourceAdd.builder().ids(cityIds).build());
        cityDefenceMapper.deleteByCdt(CGiCityDefence.builder().cityIds(cityIds).build());
        cityBuildMapper.deleteByCdt(CGiCityBuild.builder().cityIds(cityIds).build());
        cityMapper.deleteByCdt(CGiCity.builder().ids(cityIds).build());

    }

}

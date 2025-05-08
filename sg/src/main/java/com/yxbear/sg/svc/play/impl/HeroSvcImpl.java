package com.yxbear.sg.svc.play.impl;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.mapper.gi.GiCityHeroMapper;
import com.yxbear.sg.domain.model.gi.CGiCityHero;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.play.HeroSvc;
import com.yxbear.sg.svc.play.bean.CityInfo;
import com.yxbear.sg.svc.play.bean.PlayInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeroSvcImpl implements HeroSvc {

    final SgEngine sgEngine;

    final GiCityHeroMapper heroMapper;

    @Override
    public void createFirstHero(ProvinceLand land, Userable user) {
        
    }

    @Override
    public void fillHero(PlayInfo info) {
        CityInfo city = info.getCity();
        if (city == null) { return; }

        CGiCityHero cdt = new CGiCityHero();
        cdt.setCityId(info.getCity().getId());
        city.setHeros(heroMapper.queryList(cdt, "NAME"));

    }

}

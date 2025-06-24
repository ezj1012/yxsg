package com.yxbear.sg.svc.egimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiCityBuild;
import com.yxbear.sg.domain.model.gi.GiCityResource;
import com.yxbear.sg.domain.model.gi.GiCityResourceAdd;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.engine.model.SgData;
import com.yxbear.sg.svc.cfg.bean.CityCreator;
import com.yxbear.sg.svc.play.bean.RegPlay;

@Service
public class ModelFactory {

    final SgEngine engine;

    public ModelFactory(SgEngine engine) {
        super();
        this.engine = engine;
    }

    public GiPlayer withPlayer(ProvinceLand land, Userable user, RegPlay regPlay) {
        GiPlayer play = JSON.parseObject(getGameData().getPlayerTemplate(), GiPlayer.class);
        play.setId(user.getId());
        play.setFace(regPlay.getIcon());
        play.setGender(regPlay.getGender());
        play.setName(regPlay.getName());
        play.setFlagChar(regPlay.getName().substring(0, 1));
        play.setLastCity(SystemUtils.wid2cid(land.getId()));

        return play;
    }

    public SgData getGameData() {
        return engine.getCtx().getData();
    }

    public CityCreator withFirstCity(ProvinceLand land, Userable user) {
        CityCreator cc = JSON.parseObject(getGameData().getCityTemplate(), CityCreator.class);
        GiCity city = cc.getCity();
        city.setId(land.toCityId());
        city.setPlayId(user.getId());

        cc.setResource(cc.getResource() == null ? new GiCityResource() : cc.getResource());
        cc.getResource().setId(city.getId());
        cc.getResource().setLastUpdate(System.currentTimeMillis());
        cc.setResourceAdd(cc.getResourceAdd() == null ? new GiCityResourceAdd() : cc.getResourceAdd());
        cc.getResourceAdd().setId(city.getId());

        cc.setBuilds(mergeBaseBuild(cc.getBuilds()));
        cc.getBuilds().forEach(b -> b.setCityId(city.getId()));

        cc.setDefences(cc.getDefences() == null ? new ArrayList<>() : cc.getDefences());

        return cc;
    }

    private List<GiCityBuild> mergeBaseBuild(List<GiCityBuild> builds) {
        if (builds == null || builds.isEmpty()) {
            GiCityBuild b1 = new GiCityBuild();
            b1.setBid(6);
            b1.setPos(0);
            b1.setLv(1);
            b1.setStatus(0);
            GiCityBuild b2 = new GiCityBuild();
            b2.setBid(20);
            b2.setPos(1);
            b2.setLv(0);
            b2.setStatus(0);
            return new ArrayList<>(Arrays.asList(b1, b2));
        }
        return builds;
    }

    public static void main(String[] args) {
        GiCity city = new GiCity();
        city.setName("新城池");
        city.setState(1);
        city.setCityType(0);
        CityCreator c = new CityCreator();
        c.setCity(city);
        // System.out.println(JSON.toJSONString(c));
        GiCityBuild b1 = new GiCityBuild();
        b1.setBid(6);
        b1.setPos(0);
        b1.setLv(1);
        b1.setStatus(0);
        GiCityBuild b2 = new GiCityBuild();
        b2.setBid(20);
        b2.setPos(1);
        b2.setLv(0);
        b2.setStatus(0);
        c.setBuilds(Arrays.asList(b1, b2));
        System.out.println(JSON.toJSONString(c));
    }

}

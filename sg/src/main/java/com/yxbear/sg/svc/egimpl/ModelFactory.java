package com.yxbear.sg.svc.egimpl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.coder.CoderController;
import com.yxbear.core.coder.configuration.CoderConfiguration;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.engine.model.SgData;
import com.yxbear.sg.svc.cfg.bean.CityCreator;
import com.yxbear.sg.svc.play.bean.RegPlay;

@Service
public class ModelFactory {

    private final CoderController coderController;

    private final CoderConfiguration coderConfiguration;

    final SgEngine engine;

    public ModelFactory(SgEngine engine, CoderConfiguration coderConfiguration, CoderController coderController) {
        super();
        this.engine = engine;
        this.coderConfiguration = coderConfiguration;
        this.coderController = coderController;
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
        
        return cc;
    }

    public static void main(String[] args) {
        GiCity city = new GiCity();
        city.setName("新城池");
        city.setState(1);
        city.setCityType(0);
        CityCreator c = new CityCreator();
        c.setCity(city);
        System.out.println(JSON.toJSONString(c));

    }

}

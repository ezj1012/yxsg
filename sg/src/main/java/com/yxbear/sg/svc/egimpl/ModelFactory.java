package com.yxbear.sg.svc.egimpl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.engine.model.SgData;
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

}

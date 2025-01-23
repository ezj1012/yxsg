package com.yxbear.sg.svc.play.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.LoginUser;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.mapper.gi.GiPlayerMapper;
import com.yxbear.sg.domain.model.gi.CGiPlayer;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.engine.world.WorldMgr;
import com.yxbear.sg.svc.egimpl.ModelFactory;
import com.yxbear.sg.svc.play.PlayRegSvc;
import com.yxbear.sg.svc.play.bean.RegPlay;

@Service
public class PlayRegSvcImpl implements PlayRegSvc {

    ConcurrentHashMap<String, RegPlay> lockedNames = new ConcurrentHashMap<>();

    final SgEngine sgEngine;

    final GiPlayerMapper playMapper;

    final ModelFactory modelFactory;

    public PlayRegSvcImpl(SgEngine sgEngine, GiPlayerMapper playMapper, ModelFactory modelFactory) {
        super();
        this.sgEngine = sgEngine;
        this.playMapper = playMapper;
        this.modelFactory = modelFactory;
    }

    @Override
    public void regPlay(RegPlay regPlay) {
        LoginUser user = SystemUtils.getCurUser();
        final String name = regPlay.getName();
        if (lockedNames.putIfAbsent(name, regPlay) != null) { throw new ServiceException("玩家名[" + name + "]已存在!"); }

        // 如果
        WorldMgr worldMgr = sgEngine.getWorldMgr();
        ProvinceLand land = null;
        try {
            checkName(regPlay, user);
            land = worldMgr.randomAndLockLand(regPlay.getProvinceId());
            createPlayer(land, user, regPlay);
        } finally {
            lockedNames.remove(regPlay.getName());
            worldMgr.release(land);
        }
    }

    @Transactional
    public void createPlayer(ProvinceLand land, Userable user, RegPlay regPlay) {
        GiPlayer play = modelFactory.withPlayer(land, user, regPlay);
        playMapper.save(play);
        play = playMapper.selectById(play.getId());
        System.out.println("创建玩家成功:" + play);
        System.out.println();
    }

    private void checkName(RegPlay regPlay, Userable user) {
        Integer id = user.getId();
        GiPlayer play = playMapper.selectById(id);
        if (play != null) { throw new ServiceException("玩家已经存在!"); }
        CGiPlayer cdt = new CGiPlayer();
        cdt.setNameEqual(regPlay.getName());
        if (playMapper.count(cdt) >= 1) { throw new ServiceException("玩家名[" + regPlay.getName() + "]已存在!"); }
    }

}

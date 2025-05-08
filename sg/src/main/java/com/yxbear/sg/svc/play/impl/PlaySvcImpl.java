package com.yxbear.sg.svc.play.impl;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.LoginUser;
import com.yxbear.sg.domain.mapper.gi.GiPlayerMapper;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.sg.domain.model.gi.GiUnion;
import com.yxbear.sg.svc.play.CitySvc;
import com.yxbear.sg.svc.play.PlaySvc;
import com.yxbear.sg.svc.play.UnionSvc;
import com.yxbear.sg.svc.play.bean.PlayInfo;
import com.yxbear.sg.svc.play.bean.QPlayState;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaySvcImpl implements PlaySvc {

    final GiPlayerMapper playMapper;

    final UnionSvc unionSvc;

    final CitySvc citySvc;

    @Override
    public PlayInfo getPlayInfo(QPlayState state) {
        LoginUser user = SystemUtils.getCurUser();
        // deleteAll(user.getId());
        GiPlayer player = playMapper.selectById(user.getId());
        if (player == null) { return null; }

        PlayInfo info = SystemUtils.copy(player, PlayInfo.class);

        if (info.getUnionId() != 0) {
            GiUnion union = unionSvc.getUnionById(info.getUnionId());
            if (union != null) {
                info.setUnionName(union.getName());
            }
        }

        citySvc.fillCity(info);

        return info;
    }

    void deleteAll(int id) {
        citySvc.deleteAll(id);
        playMapper.deleteById(id);
    }

}

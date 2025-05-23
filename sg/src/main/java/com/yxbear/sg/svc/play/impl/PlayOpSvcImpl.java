package com.yxbear.sg.svc.play.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yxbear.sg.svc.play.CitySvc;
import com.yxbear.sg.svc.play.PlayInfoSvc;
import com.yxbear.sg.svc.play.bean.PlayInfo;
import com.yxbear.sg.svc.play.bean.QPlayState;
import org.springframework.stereotype.Service;

import com.yxbear.sg.svc.play.PlayOpSvc;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayOpSvcImpl implements PlayOpSvc {

    final CitySvc citySvc;
    final PlayInfoSvc playInfoSvc;

    @Override
    public PlayInfo doOp(QPlayState state) {
        JSONObject ob = (JSONObject) JSON.parse(state.getOpParams());
        int cid = state.getCityId();

        switch (state.getOp()) {
            case "upgradeBuilding":
                citySvc.createBuilding(cid, ob.getIntValue("pos"), ob.getIntValue("bId"));
                break;
        }


        return playInfoSvc.getPlayInfo(state);
    }

}

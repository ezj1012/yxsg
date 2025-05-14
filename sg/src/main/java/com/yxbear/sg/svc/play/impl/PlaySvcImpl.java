package com.yxbear.sg.svc.play.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.LoginUser;
import com.yxbear.sg.domain.mapper.gi.GiCityMapper;
import com.yxbear.sg.domain.mapper.gi.GiPlayerMapper;
import com.yxbear.sg.domain.mapper.gi.GiUnionMapper;
import com.yxbear.sg.domain.model.gi.CGiCity;
import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiPlayer;
import com.yxbear.sg.domain.model.gi.GiUnion;
import com.yxbear.sg.svc.play.PlayInfoSvc;
import com.yxbear.sg.svc.play.bean.CityInfo;
import com.yxbear.sg.svc.play.bean.CityIntro;
import com.yxbear.sg.svc.play.bean.PlayInfo;
import com.yxbear.sg.svc.play.bean.QPlayState;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaySvcImpl implements PlayInfoSvc {

    final GiPlayerMapper playMapper;

    final GiCityMapper cityMapper;

    final GiUnionMapper unionMapper;
    // final UnionSvc unionSvc;

    // final CitySvc citySvc;

    @Override
    public PlayInfo getPlayInfo(QPlayState state) {
        LoginUser user = SystemUtils.getCurUser();
        // deleteAll(user.getId());
        GiPlayer player = playMapper.selectById(user.getId());
        if (player == null) { return null; }

        PlayInfo info = SystemUtils.copy(player, PlayInfo.class);

        fillCity(info);
        fillUnion(info);

        return info;
    }

    private void fillCity(PlayInfo info) {
        List<GiCity> citys = cityMapper.queryList(CGiCity.builder().playId(info.getId()).build(), "ID");
        if (citys.isEmpty()) {
            // 被大空了.
            // citys = Arrays.asList(createLastCity(info));
            // return;
        }

        GiCity city = citys.stream().filter(c -> Objects.equals(c.getId(), info.getLastCity())).findFirst().orElse(citys.getFirst());
        CityInfo cityInfo = fillCityInfo(SystemUtils.copy(city, CityInfo.class));

        info.setCities(citys.stream().map(CityIntro::from).toList());
        info.setCity(cityInfo);

        //

    }

    private CityInfo fillCityInfo(CityInfo cityInfo) {
        return cityInfo;
    }

    private void fillUnion(PlayInfo info) {
        if (info.getUnionId() != 0) {
            GiUnion union = unionMapper.selectById(info.getUnionId());
            if (union != null) {
                info.setUnionName(union.getName());
            }
        }
    }

    // void deleteAll(int id) {
    // citySvc.deleteAll(id);
    // playMapper.deleteById(id);
    // }
}

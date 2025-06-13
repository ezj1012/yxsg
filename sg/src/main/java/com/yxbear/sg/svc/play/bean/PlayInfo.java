package com.yxbear.sg.svc.play.bean;

import java.util.*;

import com.yxbear.sg.domain.model.cfg.CfgNobility;
import com.yxbear.sg.domain.model.cfg.CfgOffice;
import com.yxbear.sg.domain.model.gi.GiPlayer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayInfo extends GiPlayer {

    String unionName = "";

    CityInfo city;

    List<CityIntro> cities = new ArrayList<>(Arrays.asList(new CityIntro(265138, "你好世界有你"), new CityIntro(265238, "你好世界有你2")));

    CfgNobility playerNobility;

    CfgOffice playerOffice;

    // 邮件
    boolean emailFlag;

    // 报告
    boolean reportFlag;

    // 战斗
    boolean fightFlag;

    // 好友申请
    boolean friendFlag;

    //
    Map<Integer, Integer> foods = new HashMap<>();

    //
    Map<Integer, Object> usedFoods = new HashMap<>();

    long time;

}

package com.yxbear.sg.svc.play.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yxbear.sg.domain.model.gi.GiPlayer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayInfo extends GiPlayer {

    String unionName = "";

    CityInfo city;

    List<CityIntro> cities = new ArrayList<>(Arrays.asList(new CityIntro(265138, "你好世界有你"), new CityIntro(265238, "你好世界有你2")));

    // 邮件
    boolean emailFlag;

    // 报告
    boolean reportFlag;

    // 战斗
    boolean fightFlag;

    // 好友申请
    boolean friendFlag;

}

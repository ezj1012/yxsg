package com.yxbear.sg.svc.play.bean;

import java.util.List;

import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiCityHero;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CityInfo extends GiCity {

    List<GiCityHero> heros;

}

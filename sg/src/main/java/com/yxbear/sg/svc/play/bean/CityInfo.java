package com.yxbear.sg.svc.play.bean;

import java.util.ArrayList;
import java.util.List;

import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiCityDefence;
import com.yxbear.sg.domain.model.gi.GiCityResource;
import com.yxbear.sg.domain.model.gi.GiCityResourceAdd;
import com.yxbear.sg.domain.model.gi.GiCitySoldier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CityInfo extends GiCity {

    GiCityResource res;

    GiCityResourceAdd resAdd;

    List<CityBuilding> buildings = new ArrayList<>();

    List<GiCityDefence> defences = new ArrayList<>();

    List<GiCitySoldier> soldiers = new ArrayList<>();

    List<CityHero> heros = new ArrayList<>();

    int maxOuterBuild;

}

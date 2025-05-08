package com.yxbear.sg.svc.cfg.bean;

import java.util.List;

import com.yxbear.sg.domain.model.gi.GiCity;
import com.yxbear.sg.domain.model.gi.GiCityBuild;
import com.yxbear.sg.domain.model.gi.GiCityDefence;
import com.yxbear.sg.domain.model.gi.GiCityHero;
import com.yxbear.sg.domain.model.gi.GiCityResource;
import com.yxbear.sg.domain.model.gi.GiCityResourceAdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityCreator {

    GiCity city;

    GiCityHero hero;

    GiCityResource resource;

    GiCityResourceAdd resourceAdd;

    List<GiCityDefence> defences;

    List<GiCityBuild> builds;

}

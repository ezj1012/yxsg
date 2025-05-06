package com.yxbear.sg.svc.play.bean;

import com.yxbear.sg.domain.model.gi.GiCity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityIntro {

    private Integer id;

    private String name;

    public static CityIntro from(GiCity city) {
        return new CityIntro(city.getId(), city.getName());
    }

}

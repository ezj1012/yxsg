package com.yxbear.sg.svc.play.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.domain.mapper.gi.GiCityHeroMapper;
import com.yxbear.sg.domain.model.gi.CGiCityHero;
import com.yxbear.sg.domain.model.gi.GiCityHero;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.play.HeroSvc;
import com.yxbear.sg.svc.play.bean.CityInfo;
import com.yxbear.sg.svc.play.bean.PlayInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeroSvcImpl implements HeroSvc {

    final SgEngine sgEngine;

    final GiCityHeroMapper heroMapper;

    @Override
    public void createFirstHero(ProvinceLand land, Userable user) {
        GiCityHero hero = new GiCityHero();
        hero.setId(user.getId());
        hero.setName(user.getName());
        hero.setFace(0);
        hero.setPlayId(user.getId());
        hero.setCityId(land.toCityId());
        hero.setExp(0L);
        hero.setNpcId(0);
        hero.setGender(1);
        
        hero.setCommandBase(51);
        hero.setAffairsBase(60);
        hero.setBraveryBase(60);
        hero.setWisdomBase(60);
        
        hero.setCommandAddOn(51);
        hero.setAffairsAddOn(60L);
        hero.setBraveryAddOn(60L);
        hero.setForceMaxAddOn(60);
        hero.setWisdomAddOn(60L);
        hero.setEnergyMaxAddOn(150);
        hero.setDefenceAddOn(600L);
        hero.setAttackAddOn(600L);
        
        hero.setSpeedAddOn(0);

        hero.setAffairsAdd(0);
        hero.setBraveryAdd(0);
        hero.setWisdomAdd(0);
        hero.setLoyalty(100);
     
        hero.setHeroHealth(0);
        hero.setCityType(0);
        hero.setHeroType(1000);
        hero.setState(0);
        hero.setLevel(1);
        
        heroMapper.save(hero);
    }

    public static void main(String[] args) {
      

        Method[] declaredMethods = GiCityHero.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().startsWith("set")) {
                String n = method.getParameters()[0].getParameterizedType().getTypeName();
                String t = n.equals("java.lang.String") ? "" : n.equals("java.lang.Integer") ? "0" : "0L";
                // System.out.println();
                System.out.println("hero." + method.getName() + "(" + t + ");");
            }
        }
    }

}

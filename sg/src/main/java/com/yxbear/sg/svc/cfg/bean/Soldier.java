package com.yxbear.sg.svc.cfg.bean;

import java.util.Map;

import com.yxbear.sg.domain.model.cfg.CfgSoldier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Soldier extends CfgSoldier {

    Map<Integer, Map<Integer, Integer>> preCdts;
    
}

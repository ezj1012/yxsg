package com.yxbear.sg.svc.cfg.bean;

import java.util.Map;

import com.yxbear.sg.domain.model.cfg.CfgDefence;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Defence extends CfgDefence {

    Map<Integer, Map<Integer, Integer>> preCdts;

}

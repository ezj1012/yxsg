package com.yxbear.sg.svc.cfg.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.SGProps;
import com.yxbear.sg.domain.mapper.cfg.CfgBuildingConditionMapper;
import com.yxbear.sg.domain.mapper.cfg.CfgBuildingLevelMapper;
import com.yxbear.sg.domain.mapper.cfg.CfgBuildingMapper;
import com.yxbear.sg.domain.model.cfg.CfgBuildingCondition;
import com.yxbear.sg.domain.model.cfg.CfgBuildingLevel;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.cfg.bean.Building;
import com.yxbear.sg.svc.cfg.bean.FrameCfg;
import com.yxbear.sg.svc.cfg.bean.GlobalCfg;
import com.yxbear.sg.svc.cfg.bean.PlayerIcon;

import lombok.RequiredArgsConstructor;

@Service
// @AllArgsConstructor
@RequiredArgsConstructor
public class FrameCfgSvcImpl implements FrameCfgSvc, InitializingBean {

    File frameCfgFile;

    File iconDir;

    FrameCfg frameCfg;

    GlobalCfg globalCfg;

    final SGProps sgProps;

    final CfgBuildingMapper buildingMapper;
    final CfgBuildingLevelMapper buildingLevelMapper;
    final CfgBuildingConditionMapper buildingConditionMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        frameCfgFile = new File(sgProps.getRsmDir(), "cfgFrame.json");
        iconDir = new File(sgProps.getRsmDir(), "player");
        readFrameCfg();
        readGlobalCfg();
    }

    @Override
    public FrameCfg getFrameCfg() {
        readFrameCfg(); //

        return frameCfg;
    }

    @Override
    public GlobalCfg getGlobalCfg() {
        readGlobalCfg();
        return globalCfg;
    }

    private void readGlobalCfg() {
        globalCfg = new GlobalCfg();
        PlayerIcon.refresh(iconDir);
        globalCfg.setPlayerIcons(PlayerIcon.getPlayerIcons());
        globalCfg.setBuildingsMap(readBuilding());
    }

    private Map<Integer, Building> readBuilding() {
        Map<Integer, Map<Integer, CfgBuildingLevel>> bLvMap = buildingLevelMapper.queryAll().stream()
                .collect(Collectors.groupingBy(CfgBuildingLevel::getBuildId,
                        Collectors.toMap(CfgBuildingLevel::getLevel, Function.identity())));
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> bCdtMap = new HashMap<>();
        for (CfgBuildingCondition cfgBuildingCondition : buildingConditionMapper.queryAll()) {
            if (bCdtMap.computeIfAbsent(cfgBuildingCondition.getBuildId(), k1 -> new HashMap<>()).computeIfAbsent(cfgBuildingCondition.getLevelId(), key -> new HashMap<>()).computeIfAbsent(cfgBuildingCondition.getPreType(), k -> new HashMap<>()).put(cfgBuildingCondition.getPreId(), cfgBuildingCondition.getPreLevel()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }

        Map<Integer, Building> bMap = buildingMapper.queryAll().stream()
                .map(b -> {
                    Building rb = SystemUtils.copy(b, Building.class);
                    rb.setLevels(bLvMap.get(b.getId()));
                    rb.setPreCdts(bCdtMap.get(b.getId()));
                    return rb;
                }).collect(Collectors.toMap(b -> b.getId(), b -> b));

        return bMap;
    }

    private void readFrameCfg() {
        try {
            List<String> allLines = Files.readAllLines(frameCfgFile.toPath());
            StringBuilder sb = new StringBuilder();
            for (String l : allLines) {
                if (l.trim().startsWith("//")) {
                    continue;
                }
                sb.append(l).append("\n");
            }
            frameCfg = JSON.parseObject(sb.toString(), FrameCfg.class);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

}

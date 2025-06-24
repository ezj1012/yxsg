package com.yxbear.sg.svc.cfg.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.yxbear.sg.domain.mapper.cfg.*;
import com.yxbear.sg.domain.model.cfg.*;
import com.yxbear.sg.svc.cfg.bean.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.SGProps;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;

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

    final CfgTechnicMapper technicMapper;

    final CfgTechnicLevelMapper technicLevelMapper;

    final CfgTechnicConditionMapper technicConditionMapper;

    final CfgNobilityMapper nobilityMapper;

    final CfgOfficeMapper officeMapper;

    final CfgSoldierMapper soldierMapper;

    final CfgSoldierConditionMapper soldierConditionMapper;

    final CfgDefenceMapper defenceMapper;

    final CfgDefenceConditionMapper defenceConditionMapper;

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
        if (globalCfg == null || globalExpired()) {
            globalCfg = new GlobalCfg();
            PlayerIcon.refresh(iconDir);
            globalCfg.setPlayerIcons(PlayerIcon.getPlayerIcons());
            globalCfg.setBuildingsMap(readBuilding());
            globalCfg.setTechnicsMap(readTechnics());
            globalCfg.setSoldiersMap(readSoldiers());
            globalCfg.setDefencesMap(readDefences());

            LinkedHashMap<Integer, CfgNobility> nobilityMap = new LinkedHashMap<>();
            LinkedHashMap<Integer, CfgOffice> officeMap = new LinkedHashMap<>();
            nobilityMapper.queryList(CCfgNobility.builder().build(), "ID").forEach(n -> nobilityMap.put(n.getId(), n));
            officeMapper.queryList(CCfgOffice.builder().build(), "ID").forEach(n -> officeMap.put(n.getId(), n));
            globalCfg.setNobilityMap(nobilityMap);
            globalCfg.setOfficeMap(officeMap);
        }
    }

    private boolean globalExpired() {
        return false;
    }

    private Map<Integer, Technic> readTechnics() {
        Map<Integer, Map<Integer, CfgTechnicLevel>> bLvMap = technicLevelMapper.queryAll().stream().collect(Collectors.groupingBy(CfgTechnicLevel::getTechnicId, Collectors.toMap(CfgTechnicLevel::getLevel, Function.identity())));
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> bCdtMap = new HashMap<>();
        for (CfgTechnicCondition cfgTechnicCondition : technicConditionMapper.queryAll()) {
            if (bCdtMap.computeIfAbsent(cfgTechnicCondition.getTechnicId(), k1 -> new HashMap<>()).computeIfAbsent(cfgTechnicCondition.getLevelId(), key -> new HashMap<>()).computeIfAbsent(cfgTechnicCondition.getPreType(), k -> new HashMap<>()).put(cfgTechnicCondition.getPreId(),
                    cfgTechnicCondition.getPreLevel()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }

        return technicMapper.queryAll().stream().map(b -> {
            Technic rb = SystemUtils.copy(b, Technic.class);
            rb.setLevels(bLvMap.get(b.getId()));
            rb.setPreCdts(bCdtMap.get(b.getId()));
            return rb;
        }).collect(Collectors.toMap(CfgTechnic::getId, b -> b));
    }

    private Map<Integer, Defence> readDefences() {
        Map<Integer, Map<Integer, Map<Integer, Integer>>> pCdtMap = new HashMap<>();
        for (CfgDefenceCondition cdt : defenceConditionMapper.queryAll()) {
            if (pCdtMap.computeIfAbsent(cdt.getDefenceId(), k1 -> new HashMap<>()).computeIfAbsent(cdt.getPreType(), key -> new HashMap<>()).put(cdt.getPreId(), cdt.getPreLevel()) != null) { throw new IllegalStateException("Duplicate key"); }
        }
        return defenceMapper.queryAll().stream().map(b -> {
            Defence rb = SystemUtils.copy(b, Defence.class);
            rb.setPreCdts(pCdtMap.get(rb.getId()));
            return rb;
        }).collect(Collectors.toMap(CfgDefence::getId, b -> b));
    }

    private Map<Integer, Soldier> readSoldiers() {
        Map<Integer, Map<Integer, Map<Integer, Integer>>> pCdtMap = new HashMap<>();
        for (CfgSoldierCondition cdt : soldierConditionMapper.queryAll()) {
            if (pCdtMap.computeIfAbsent(cdt.getSoldierId(), k1 -> new HashMap<>()).computeIfAbsent(cdt.getPreType(), key -> new HashMap<>()).put(cdt.getPreId(), cdt.getPreLevel()) != null) { throw new IllegalStateException("Duplicate key"); }
        }
        return soldierMapper.queryAll().stream().map(b -> {
            Soldier rb = SystemUtils.copy(b, Soldier.class);
            rb.setPreCdts(pCdtMap.get(rb.getId()));
            return rb;
        }).collect(Collectors.toMap(CfgSoldier::getId, b -> b));

    }

    private Map<Integer, Building> readBuilding() {
        Map<Integer, Map<Integer, CfgBuildingLevel>> bLvMap = buildingLevelMapper.queryAll().stream().collect(Collectors.groupingBy(CfgBuildingLevel::getBuildId, Collectors.toMap(CfgBuildingLevel::getLevel, Function.identity())));
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> bCdtMap = new HashMap<>();
        for (CfgBuildingCondition cfgBuildingCondition : buildingConditionMapper.queryAll()) {
            if (bCdtMap.computeIfAbsent(cfgBuildingCondition.getBuildId(), k1 -> new HashMap<>()).computeIfAbsent(cfgBuildingCondition.getLevelId(), key -> new HashMap<>()).computeIfAbsent(cfgBuildingCondition.getPreType(), k -> new HashMap<>())
                    .put(cfgBuildingCondition.getPreId(), cfgBuildingCondition.getPreLevel()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }

        return buildingMapper.queryAll().stream().map(b -> {
            Building rb = SystemUtils.copy(b, Building.class);
            rb.setLevels(bLvMap.get(b.getId()));
            rb.setPreCdts(bCdtMap.get(b.getId()));
            return rb;
        }).collect(Collectors.toMap(CfgBuilding::getId, b -> b));
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

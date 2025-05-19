package com.yxbear.sg.svc.egimpl.jobs;

import com.yxbear.core.CommUtils;
import com.yxbear.sg.domain.mapper.mem.MemCityBuildUpgradingMapper;
import com.yxbear.sg.domain.model.mem.CMemCityBuildUpgrading;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.svc.play.CitySvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BuildingUpgradeJob {

    final SgEngine sg;
    final CitySvc citySvc;

    final MemCityBuildUpgradingMapper buMapper;

    @Transactional(readOnly = true)
    public void processBuilding() {
        if (!sg.isRunning()) {
            return;
        }
        long time = System.currentTimeMillis();
        try {
            List<MemCityBuildUpgrading> list = buMapper.queryList(CMemCityBuildUpgrading.builder().endEndTime(time).build(), "id");
            if (!CommUtils.isEmpty(list)) {
                list.forEach(item -> sg.getCtx().getExecutor().execute(() -> {
                    try {
                        citySvc.finishBuildUpgrading(item);
                    } catch (Exception e) {
                        log.error("处理失败: {}", item.getId(), e);
                    }
                }));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

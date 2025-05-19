package com.yxbear.sg.svc.egimpl;

import com.yxbear.sg.svc.egimpl.jobs.BuildingUpgradeJob;
import org.springframework.stereotype.Service;

import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.loader.SgJobWorker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SgJobRegisterImpl extends SgJobWorker {

    final SgEngine sg;
    final BuildingUpgradeJob processBuilding;

    @Override
    public synchronized void updateJobPeriod() {
        // 使用Spring 的 Schedule 对
        this.schedule(new SgJobWorker.Task("processBuilding", processBuilding::processBuilding),
                Math.min(1, sg.getCtx().getData().getJobBuild()));
    }


}

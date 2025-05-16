package com.yxbear.sg.engine;

import com.yxbear.sg.engine.loader.SgDataLoader;
import com.yxbear.sg.engine.loader.SgJobWorker;
import com.yxbear.sg.engine.loader.SgWorldDataLoader;
import com.yxbear.sg.engine.model.SgData;

import lombok.Getter;
import org.springframework.context.ApplicationContext;


public class SgContext {
    @Getter
    ApplicationContext appCtx;

    @Getter
    SgDataLoader dataMgr;

    @Getter
    SgWorldDataLoader worldDataLoader;

    SgJobWorker jobWorker;

    @Getter
    SgData data;

    public SgContext(ApplicationContext applicationContext) {
        super();
        appCtx = applicationContext;
    }

    public synchronized void init() {
        dataMgr = appCtx.getBean(SgDataLoader.class);
        worldDataLoader = appCtx.getBean(SgWorldDataLoader.class);
        jobWorker = appCtx.getBean(SgJobWorker.class);
        data = dataMgr.load();

        this.updateJobPeriod();
    }

    public synchronized void updateJobPeriod() {
        // 使用Spring 的 Schedule 对
        jobWorker.schedule(new SgJobWorker.Task("processBuilding", jobWorker::processBuilding), Math.min(1, data.getJobBuild()));

    }

}

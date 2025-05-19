package com.yxbear.sg.engine;

import com.yxbear.sg.engine.loader.SgDataLoader;
import com.yxbear.sg.engine.loader.SgJobWorker;
import com.yxbear.sg.engine.loader.SgWorldDataLoader;
import com.yxbear.sg.engine.model.SgData;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;

public class SgContext implements AutoCloseable {
    @Getter
    ApplicationContext appCtx;

    @Getter
    SgDataLoader dataMgr;

    @Getter
    SgWorldDataLoader worldDataLoader;

    SgJobWorker jobWorker;

    @Getter
    SgData data;

    @Getter
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public SgContext(ApplicationContext applicationContext) {
        super();
        appCtx = applicationContext;
    }

    public synchronized void init() {
        dataMgr = appCtx.getBean(SgDataLoader.class);
        worldDataLoader = appCtx.getBean(SgWorldDataLoader.class);
        jobWorker = appCtx.getBean(SgJobWorker.class);
        data = dataMgr.load();
        jobWorker.updateJobPeriod();
    }

    public void publishEvent(SgContextEvent event) {
        executor.execute(() -> {
            try {
                this.appCtx.publishEvent(event);
            } catch (Exception e) {
            }
        });
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
    }

}

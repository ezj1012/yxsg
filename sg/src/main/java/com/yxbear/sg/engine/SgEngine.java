package com.yxbear.sg.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.SmartLifecycle;

import com.yxbear.sg.engine.world.WorldMgr;

public class SgEngine implements SmartLifecycle, Mgrable {

    private static SgEngine instance;

    private volatile boolean running;

    SgContext ctx;

    WorldMgr worldMgr;

    List<Mgrable> managers = new ArrayList<>();

    public SgEngine(SgContext ctx) {
        if (instance != null) { throw new IllegalStateException("SgEngine already created"); }
        instance = this;
        this.ctx = ctx;
        worldMgr = new WorldMgr(ctx);
        managers.add(worldMgr);
    }

    public static SgEngine getInstance() {
        return instance;
    }

    public void start() {
        this.running = true;
        this.init();
        this.ctx.init();
        managers.forEach(Mgrable::init);
    }

    @Override
    public SgContext getCtx() {
        return ctx;
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    public WorldMgr getWorldMgr() {
        return worldMgr;
    }

}

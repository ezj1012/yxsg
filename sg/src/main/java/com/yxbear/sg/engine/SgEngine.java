package com.yxbear.sg.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.SmartLifecycle;

import com.yxbear.sg.engine.world.WorldMgr;

public class SgEngine implements SmartLifecycle, Mgrable {

    private static SgEngine instance;

    private volatile boolean running;
    private volatile boolean started;

    SgContext ctx;

    WorldMgr worldMgr;

    List<Mgrable> managers = new ArrayList<>();

    public SgEngine(SgContext ctx) {
        if (instance != null) {
            throw new IllegalStateException("SgEngine already created");
        }
        instance = this;
        this.ctx = ctx;
        worldMgr = new WorldMgr(ctx);
        managers.add(worldMgr);
    }

    public static SgEngine getInstance() {
        return instance;
    }

    public void start() {
        if (this.started || this.running) {
            throw new IllegalStateException("SgEngine already started");
        }
        this.started = true;
        this.ctx.init();
        this.init();
        managers.forEach(Mgrable::init);
        this.running = true;
    }

    @Override
    public SgContext getCtx() {
        return ctx;
    }

    public PlayContext getPlayCtx(int playId) {
        return new PlayContext(playId, ctx);
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

    public void event(Object mb) {
        ctx.publishEvent(new SgContextEvent(mb));
    }
}

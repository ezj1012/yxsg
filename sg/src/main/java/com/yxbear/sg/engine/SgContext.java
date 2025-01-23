package com.yxbear.sg.engine;

import com.yxbear.sg.engine.loader.SgDataLoader;
import com.yxbear.sg.engine.loader.SgWorldDataLoader;
import com.yxbear.sg.engine.model.SgData;

import lombok.Getter;

public class SgContext {

    @Getter
    SgDataLoader dataMgr;

    @Getter
    SgWorldDataLoader worldDataLoader;

    @Getter
    SgData data;

    public SgContext(SgDataLoader dataMgr, SgWorldDataLoader worldDataLoader) {
        super();
        this.dataMgr = dataMgr;
        this.worldDataLoader = worldDataLoader;
    }

    public synchronized void init() {
        data = dataMgr.load();
    }

}

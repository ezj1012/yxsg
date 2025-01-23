package com.yxbear.sg.engine.loader;

import com.yxbear.sg.engine.model.SgData;

public interface SgDataLoader {

    SgData load();

    void save(SgData data);

}

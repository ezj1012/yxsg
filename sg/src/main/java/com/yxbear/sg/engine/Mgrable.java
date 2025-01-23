package com.yxbear.sg.engine;

public interface Mgrable {

    SgContext getCtx();

    default void init() {
    }

}

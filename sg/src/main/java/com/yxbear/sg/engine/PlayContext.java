package com.yxbear.sg.engine;

import com.yxbear.core.CommUtils;

import java.util.Map;

public class PlayContext {
    int playId;
    SgContext ctx;

    public PlayContext(int playId, SgContext ctx) {
        this.playId = playId;
        this.ctx = ctx;
    }

    public boolean hasUsingGoods(int... gids) {
        return true;
    }

    public boolean hasUsingGoods(int gid) {
        return true;
    }

    public boolean checkGoods(Map<Integer, Integer> useGoods) {
        return true;
    }

    public boolean checkTech(Integer collegelv, Map<Integer, Integer> integerIntegerMap) {
        if (CommUtils.isEmpty(integerIntegerMap)) return true;

        return true;
    }

    public int getTechLv(int buildTechId) {
        return 0;
    }

    public void use(Map<Integer, Integer> useGoods) {
        if (CommUtils.isEmpty(useGoods)) {
            return;
        }
    }

    /**
     * 56	徭役令 5个
     * 166	高级徭役令 7个
     *
     * @param curCount 当前建筑队列数量
     * @return
     */
    public boolean checkBuildingCount(int curCount) {
        return curCount < 2 || hasUsingGoods(166) && curCount < 7 || hasUsingGoods(56) && curCount < 5;
    }
}

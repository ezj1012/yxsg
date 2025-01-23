package com.yxbear.sg.engine.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.engine.Mgrable;
import com.yxbear.sg.engine.SgContext;
import com.yxbear.sg.engine.model.Province;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.engine.model.WorldTile;

import lombok.Getter;

public class WorldMgr implements Mgrable {

    @Getter
    final SgContext ctx;

    final ProvinceLandMgr provinceLandMgr;

    public WorldMgr(SgContext ctx) {
        super();
        this.ctx = ctx;
        provinceLandMgr = new ProvinceLandMgr(this);
    }

    @Override
    public void init() {
        provinceLandMgr.init();
    }

    public List<Province> getRestierProvinces() {
        return ctx.getData().getProvinces().stream().filter(f -> !f.isMinority()).toList();
    }

    public ProvinceLand randomAndLockLand(int provinceId) {
        if (provinceId == 0) {
            provinceId = new Random().nextInt(13) % 13 + 1;
        }
        return provinceLandMgr.getAndLock(provinceId);
    }

    public void release(int wid) {
        provinceLandMgr.release(wid);
    }

    public void release(ProvinceLand land) {
        provinceLandMgr.release(land);
    }

    public List<WorldTile> getWorldTiles(int x, int y, int xw, int yw) {
        List<Integer> validIds = new ArrayList<>();
        validIds.add(SystemUtils.wid(x, y));

        int my = y - yw + xw;
        int mx = x - yw - xw;
        List<Integer> allIds = new ArrayList<>();
        for (int j = 0; j < yw * 4 + 1; j++) {
            boolean t = j % 2 == 0;
            for (int i = 0; i < xw * 2 + 1; i++) {
                int nx = mx + i + j / 2;
                int ny = t ? my - i + j / 2 : my - i + j / 2 + 1;
                int id = SystemUtils.wid(nx, ny);
                allIds.add(id);
                if (nx > 0 && nx < 501 && ny > 0 && ny < 501) {
                    validIds.add(id);
                }
            }
        }

        List<WorldTile> tiles = getCtx().getWorldDataLoader().loadTiles(validIds);

        Map<Integer, WorldTile> idMap = tiles.stream().collect(Collectors.toMap(WorldTile::getId, a -> a));
        return allIds.stream().map(id -> idMap.computeIfAbsent(id, i -> new WorldTile(i))).toList();
    }

}

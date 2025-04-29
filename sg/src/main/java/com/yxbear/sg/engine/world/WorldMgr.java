package com.yxbear.sg.engine.world;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<Integer> result = new ArrayList<>();
        Map<Integer, String> errMap = new HashMap<>();
        int sx = x - xw;
        int sy = y + xw;

        sx -= (yw + 1) / 2;
        sy -= (yw + 1) / 2;

        boolean curLeft = yw % 2 == 1;
        if (curLeft) {
            sy += 1;
        }

        int yh = yw * 2 + 1;
        int xh = xw * 2 + 1;

        for (int i = 0; i < yh; i++) {
            int nx = sx;
            int ny = sy;
            // if(!curLeft) {
            // System.out.print(" ");
            // }
            for (int j = 0; j < xh; j++) {
                int cx = nx + j;
                int cy = ny - j;
                // System.out.print(cx+"_"+cy+"|");
                int wid = SystemUtils.wid(cx, cy);
                result.add(wid);
                if (cx > 0 && cx <= 500 && cy > 0 && cy <= 500) {
                    validIds.add(wid);
                } else {
                    errMap.put(wid, cx + "," + cy);
                }
            }
            // System.out.println();
            if (curLeft) {
                sx += 1;
            } else {
                sy += 1;
            }
            curLeft = !curLeft;
        }
        Random random = new Random();

        List<WorldTile> tiles = getCtx().getWorldDataLoader().loadTiles(validIds);
        tiles.forEach(t -> t.setCurLevel(random.nextInt(30)));
        Map<Integer, WorldTile> idMap = tiles.stream().collect(Collectors.toMap(WorldTile::getId, a -> a));
        return result.stream().map(id -> idMap.computeIfAbsent(id, id2 -> new WorldTile(id2, errMap.get(id2)))).toList();
    }

}

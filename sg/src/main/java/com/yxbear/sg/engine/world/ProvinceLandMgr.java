package com.yxbear.sg.engine.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.engine.model.Province;
import com.yxbear.sg.engine.model.ProvinceLand;

public class ProvinceLandMgr {

    private static final long THRESHOLD = 3;

    final WorldMgr worldMgr;

    Map<Integer, ProvinceLandCache> provinceLandMap;

    // 被申请使用的平地
    ConcurrentHashMap<Integer, ProvinceLand> lockedProvinceLandMap = new ConcurrentHashMap<>();

    public ProvinceLandMgr(WorldMgr worldMgr) {
        this.worldMgr = worldMgr;
    }

    //
    public void init() {
        // 初始化可用平地
        Map<Integer, ProvinceLandCache> tempMap = new HashMap<>();

        worldMgr.getCtx().getWorldDataLoader(). // 加载可用平地
                loadAvailableLands().forEach((provinceId, landIds) -> tempMap.put(provinceId, new ProvinceLandCache(provinceId, landIds)));
        provinceLandMap = Collections.unmodifiableMap(tempMap);
    }

    public ProvinceLand getAndLock(final int provinceId) {
        ProvinceLandCache pLands = provinceLandMap.get(provinceId);
        if (pLands == null) { throw new ServiceException("没有找到对应的州!"); }
        ProvinceLand provinceLand = pLands.get();
        lockedProvinceLandMap.put(provinceLand.getId(), provinceLand);
        return provinceLand;
    }

    public boolean isLocked(int landId) {
        return lockedProvinceLandMap.containsKey(landId);
    }

    /**
     * 释放平地
     *
     * @param provinceLand
     */
    public void release(ProvinceLand provinceLand) {
        if (provinceLand != null) {
            lockedProvinceLandMap.remove(provinceLand.getId());
            provinceLandMap.get(provinceLand.getProvince().getId()).remove(provinceLand.getId());
        }
    }

    public void release(int provinceId, int landId) {
        lockedProvinceLandMap.remove(landId);
        provinceLandMap.get(provinceId).remove(landId);
    }

    //
    /**
     * 释放平地
     *
     * @param id
     *            平地id
     */
    public void release(int id) {
        ProvinceLand remove = lockedProvinceLandMap.remove(id);
        if (remove != null) {
            provinceLandMap.get(remove.getProvince().getId()).remove(id);
        } else {
            provinceLandMap.values().forEach(p -> p.remove(id));
        }
    }

    class ProvinceLandCache {

        private final ReentrantLock lock = new ReentrantLock();

        final int provinceId;

        Queue<ProvinceLand> availableLands = new ConcurrentLinkedQueue<>();

        volatile boolean loading = false;

        ProvinceLandCache(int provinceId, List<Integer> landIds) {
            this.provinceId = provinceId;
            landIds.stream().map(id -> new ProvinceLand(id, worldMgr.ctx.getData().getProvinces().get(provinceId))).forEach(availableLands::add);
        }

        ProvinceLand get() {
            ProvinceLand poll = availableLands.poll();
            if (poll != null) {
                if (availableLands.size() < THRESHOLD) {
                    checkAndLoadLandsAsync();
                }
                return poll;
            }
            lock.lock();
            try {
                poll = availableLands.poll();
                if (poll != null) { return poll; }

                // 重新加载
                List<Integer> lockedIds = lockedProvinceLandMap.values().stream().filter(p -> p.getProvince().getId() == provinceId).map(ProvinceLand::getId).toList();
                List<Integer> landIds = worldMgr.ctx.getWorldDataLoader().loadAvailableLands(provinceId, lockedIds);
                if (landIds.isEmpty()) { throw new ServiceException("本州没有空闲的地方了!"); }
                final Province province = worldMgr.ctx.getData().getProvinces().get(provinceId);
                landIds.stream().map(id -> new ProvinceLand(id, province)).forEach(availableLands::add);

                return availableLands.poll();
            } finally {
                lock.unlock();
            }
        }

        void remove(int id) {
            availableLands.removeIf(p -> p.getId() == id);
        }

        void checkAndLoadLandsAsync() {
            if (!loading) {
                loading = true;
                CompletableFuture.runAsync(() -> {
                    try {
                        List<Integer> lockedIds = new ArrayList<>(lockedProvinceLandMap.values().stream().filter(p -> p.getProvince().getId() == provinceId).map(ProvinceLand::getId).toList());
                        lockedIds.addAll(availableLands.stream().map(ProvinceLand::getId).toList());
                        List<Integer> newLands = worldMgr.ctx.getWorldDataLoader().loadAvailableLands(provinceId, lockedIds);
                        newLands.stream().map(id -> new ProvinceLand(id, worldMgr.ctx.getData().getProvinces().get(provinceId))).forEach(availableLands::add);
                    } finally {
                        loading = false;
                    }
                });
            }
        }

    }

}

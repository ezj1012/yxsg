package com.yxbear.sg.engine.loader;

import java.util.List;
import java.util.Map;

import com.yxbear.sg.engine.model.WorldTile;

public interface SgWorldDataLoader {

    /**
     * 随机并锁定平地
     * 
     * @param provinceIds
     * @return map key:provinceId value: 各个州的平地id
     */
    Map<Integer, List<Integer>> loadAvailableLands();

    /**
     * 加载可用的平地
     * 
     * @param provinceId
     *            州id
     * @param lockedIds
     *            锁定的平地id
     * @return 返回10个平地id
     */
    List<Integer> loadAvailableLands(int provinceId, List<Integer> lockedIds);

    /**
     * 
     * @param ids
     * @return
     */
    List<WorldTile> loadTiles(List<Integer> ids);

}

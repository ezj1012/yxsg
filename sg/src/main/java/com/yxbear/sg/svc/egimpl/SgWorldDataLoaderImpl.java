package com.yxbear.sg.svc.egimpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.mapper.mem.MemWorldTileExtMapper;
import com.yxbear.sg.domain.mapper.mem.MemWorldTileMapper;
import com.yxbear.sg.domain.model.mem.CMemWorldTile;
import com.yxbear.sg.domain.model.mem.MemWorldTile;
import com.yxbear.sg.engine.loader.SgWorldDataLoader;
import com.yxbear.sg.engine.model.WorldTile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SgWorldDataLoaderImpl implements SgWorldDataLoader {

    MemWorldTileMapper tileMapper;

    MemWorldTileExtMapper tileExtMapper;

    @Override
    public List<WorldTile> loadTiles(List<Integer> ids) {
        CMemWorldTile cdt = new CMemWorldTile();
        cdt.setIds(ids.toArray(Integer[]::new));
        return tileMapper.queryList(cdt, "ID").stream().map(b -> SystemUtils.copy(b, WorldTile.class)).toList();
    }

    @Override
    public Map<Integer, List<Integer>> loadAvailableLands() {
        return tileExtMapper.loadAvailableLands().stream().collect(Collectors.groupingBy(MemWorldTile::getProvince, Collectors.mapping(MemWorldTile::getId, Collectors.toList())));
    }

    @Override
    public List<Integer> loadAvailableLands(int provinceId, List<Integer> lockedIds) {
        return tileExtMapper.loadAvailableLandsByProvinceId(provinceId, lockedIds);
    }

}

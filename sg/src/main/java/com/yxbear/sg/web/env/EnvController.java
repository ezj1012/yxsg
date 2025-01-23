package com.yxbear.sg.web.env;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yxbear.core.bean.R;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.Province;
import com.yxbear.sg.engine.model.WorldTile;
import com.yxbear.sg.svc.play.bean.QMapIdx;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/env")
@AllArgsConstructor
public class EnvController {

    SgEngine sgEngine;

    @GetMapping("/provinces")
    public R<List<Province>> getProvinces() {
        return R.of(sgEngine.getWorldMgr().getRestierProvinces());
    }

    @PostMapping("maptiles")
    public R<List<WorldTile>> getMapTile(@RequestBody QMapIdx idx) {
        List<WorldTile> worldTiles = sgEngine.getWorldMgr().getWorldTiles(idx.getX(), idx.getY(), idx.getXw(), idx.getYw());
        return R.of(worldTiles);
    }

}

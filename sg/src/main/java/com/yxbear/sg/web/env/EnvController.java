package com.yxbear.sg.web.env;

import java.util.List;

import com.yxbear.sg.svc.play.CommQuerySvc;
import com.yxbear.sg.svc.play.DerateSvc;
import lombok.RequiredArgsConstructor;
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


@RestController
@RequestMapping("${sg.api:}/env")
@RequiredArgsConstructor
public class EnvController {

    final SgEngine sgEngine;

    final DerateSvc derateSvc;
    final CommQuerySvc commQuerySvc;

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

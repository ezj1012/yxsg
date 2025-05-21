package com.yxbear.sg.web.cfg;

import com.yxbear.core.CommUtils;
import com.yxbear.sg.domain.mapper.cfg.CfgGoodsMapper;
import com.yxbear.sg.domain.model.cfg.CCfgGoods;
import com.yxbear.sg.domain.model.cfg.CfgGoods;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.yxbear.core.bean.R;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.cfg.bean.FrameCfg;
import com.yxbear.sg.svc.cfg.bean.GlobalCfg;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/cfg")
@RequiredArgsConstructor
public class FrameCfgController {

    final FrameCfgSvc frameCfgSvc;

    final CfgGoodsMapper goodsMapper;

    @GetMapping("/frame")
    public R<FrameCfg> getFrameCfg() {
        return R.of(frameCfgSvc.getFrameCfg());
    }

    @GetMapping("/global")
    public R<GlobalCfg> getGlobalCfg() {
        return R.of(frameCfgSvc.getGlobalCfg());
    }

    @PostMapping("/goods")
    public R<List<CfgGoods>> getGoods(@RequestBody List<Integer> ids) {
        if (CommUtils.isEmpty(ids)) {
            return R.of(Collections.emptyList());
        }
        return R.of(goodsMapper.queryList(CCfgGoods.builder().ids(ids.toArray(Integer[]::new)).build(), "id"));
    }
}

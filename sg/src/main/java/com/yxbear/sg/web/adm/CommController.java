package com.yxbear.sg.web.adm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yxbear.core.bean.R;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.play.CitySvc;
import com.yxbear.sg.svc.play.CommQuerySvc;
import com.yxbear.sg.svc.play.bean.CityInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/adm")
@RequiredArgsConstructor
public class CommController {
    final SgEngine sg;
    final CommQuerySvc querySvc;
    final CitySvc citySvc;
    final FrameCfgSvc cfgSvc;

    @GetMapping({
            "/cfg/build",
            "/cfg/build/{bid}",
    })
    public R<Object> getbuild(@PathVariable(required = false) Integer bid) {
        return R.of(
                bid == null ? cfgSvc.getGlobalCfg().getBuildingsMap() : cfgSvc.getGlobalCfg().getAndCheckBuilding(bid));
    }

    @GetMapping("/city/{cid}")
    public R<CityInfo> getOrCity(@PathVariable int cid) {
        CityInfo info = querySvc.getCityInfo(cid);
        if (info == null) {
            synchronized (CommController.class) {
                if (info == null) {
                    ProvinceLand land = sg.getWorldMgr().getLand(SystemUtils.cid2wid(cid));

                    Userable user = new Userable() {

                        @Override
                        public Integer getId() {
                            return 0;
                        }

                        @Override
                        public String getName() {
                            return "";
                        }

                    };
                    citySvc.createFirstCity(land, user);
                    info = querySvc.getCityInfo(cid);
                }
            }
        }
        return R.of(info);
    }

    @GetMapping("/city/{cid}/{pos}/{bid}")
    public R<CityInfo> getMethodName(@PathVariable int cid, @PathVariable int pos, @PathVariable int bid) {
        citySvc.createBuilding(cid, pos, bid);
        return R.of(querySvc.getCityInfo(cid));
    }

    @GetMapping("/city/{cid}/{cbId}/up")
    public R<CityInfo> buildUp(@PathVariable int cid, @PathVariable int cbId) {
        citySvc.upgradeBuilding(cid, cbId);
        return R.of(querySvc.getCityInfo(cid));
    }

}

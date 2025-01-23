package com.yxbear.sg.web.cfg;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yxbear.core.bean.R;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.cfg.bean.FrameCfg;
import com.yxbear.sg.svc.cfg.bean.GlobalCfg;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cfg")
@AllArgsConstructor
public class FrameCfgController {

    FrameCfgSvc frameCfgSvc;

    @GetMapping("/frame")
    public R<FrameCfg> getFrameCfg() {
        return R.of(frameCfgSvc.getFrameCfg());
    }

    @GetMapping("/global")
    public R<GlobalCfg> getGlobalCfg() {
        return R.of(frameCfgSvc.getGlobalCfg());
    }

}

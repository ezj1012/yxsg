package com.yxbear.sg.svc.cfg.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.bean.SGProps;
import com.yxbear.sg.svc.cfg.FrameCfgSvc;
import com.yxbear.sg.svc.cfg.bean.FrameCfg;
import com.yxbear.sg.svc.cfg.bean.GlobalCfg;
import com.yxbear.sg.svc.cfg.bean.PlayerIcon;

@Service
// @AllArgsConstructor
public class FrameCfgSvcImpl implements FrameCfgSvc, InitializingBean {

    SGProps sgProps;

    File frameCfgFile;

    File iconDir;

    FrameCfg frameCfg;

    GlobalCfg globalCfg;

    public FrameCfgSvcImpl(SGProps sgProps) {
        super();
        this.sgProps = sgProps;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        frameCfgFile = new File(sgProps.getRsmDir(), "cfgFrame.json");
        iconDir = new File(sgProps.getRsmDir(), "player");
        readFrameCfg();
        readGlobalCfg();
    }

    @Override
    public FrameCfg getFrameCfg() {
        readFrameCfg(); //

        return frameCfg;
    }

    @Override
    public GlobalCfg getGlobalCfg() {
        readGlobalCfg();
        return globalCfg;
    }

    private void readGlobalCfg() {
        globalCfg = new GlobalCfg();
        PlayerIcon.refresh(iconDir);
        globalCfg.setPlayerIcons(PlayerIcon.getPlayerIcons());
    }

    private void readFrameCfg() {
        try {
            List<String> allLines = Files.readAllLines(frameCfgFile.toPath());
            StringBuilder sb = new StringBuilder();
            for (String l : allLines) {
                if (l.trim().startsWith("//")) {
                    continue;
                }
                sb.append(l).append("\n");
            }
            frameCfg = JSON.parseObject(sb.toString(), FrameCfg.class);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

}

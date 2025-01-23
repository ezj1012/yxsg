package com.yxbear.sg.engine.support;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.engine.loader.SgDataLoader;
import com.yxbear.sg.engine.model.SgData;

public class DefaultSgDataLoader implements SgDataLoader {

    Path cfgFilePath = new File("./data/sg.json").toPath();

    @Override
    public SgData load() {
        try {
            return JSON.parseObject(Files.readAllBytes(cfgFilePath), SgData.class);
        } catch (IOException e) {
            throw new ServiceException("数据异常!");
        }
    }

    @Override
    public void save(SgData data) {
        try {
            Files.writeString(cfgFilePath, JSON.toJSONString(data));
        } catch (IOException e) {
            throw new ServiceException("数据异常!");
        }
    }

}

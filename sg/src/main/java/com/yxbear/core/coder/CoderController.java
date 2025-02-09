package com.yxbear.core.coder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.bean.R;
import com.yxbear.core.coder.bean.CodeCfg;
import com.yxbear.core.coder.configuration.ProjectProps;
import com.yxbear.core.coder.gen.GenModel;
import com.yxbear.core.coder.gen.GenModelGen;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/code")
@Slf4j
public class CoderController implements InitializingBean {

    @Autowired
    ProjectProps props;

    GenModelGen modelGen;

    Path path;

    @Override
    public void afterPropertiesSet() throws Exception {
        path = new File(props.getCfgPath()).toPath();
        modelGen = new GenModelGen(props);
        log.info("代码生成器加载成功!========================>");
    }

    @GetMapping("/get")
    public R<CodeCfg> get() {
        return R.ok(getFile());
    }

    @PostMapping("/save")
    public R<CodeCfg> save(@RequestBody CodeCfg cfg) {
        saveFile(cfg);
        return R.ok(getFile());
    }

    @PostMapping("/gen")
    public void genCode(@RequestBody List<GenModel> cfgs) {
        if (cfgs.isEmpty()) {

        }
        cfgs.forEach(modelGen::gen);
    }

    private void saveFile(CodeCfg cfg) {
        try {
            Files.write(path, JSON.toJSONBytes(cfg));
        } catch (IOException e) {
        }
    }

    private CodeCfg getFile() {
        try {
            return JSON.parseObject(Files.readAllBytes(path), CodeCfg.class);
        } catch (IOException e) {
            CodeCfg codeCfg = new CodeCfg();
            codeCfg.setEntities(new ArrayList<>());
            codeCfg.setGraphs(new ArrayList<>());
            return codeCfg;
        }
    }

}

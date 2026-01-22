package com.yxbear.core.coder.bean;

import java.util.List;

import com.yxbear.core.coder.configuration.ProjectProps;

import lombok.Data;

@Data
public class CodeCfg {

    ProjectProps project;

    List<EntityCfg> entities;

    List<GraphCfg> graphs;

}

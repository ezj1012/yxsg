package com.yxbear.core.coder.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "coder")
public class ProjectProps {

    String name;

    String desc;

    String javaPackage;

    String cfgPath = "./data/coder.json";

}

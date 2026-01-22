package com.yxbear.core.coder.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "coder")
public class ProjectProps {

    /** 项目名,文件会生成在对应项目的目录下 */
    String name;

    String desc;
    /** JAVA包名 例如com.yxbear.sg ,代码会生成在com.yxbear.sg.domain目录下 */
    String javaPackage;

    String cfgPath = "./data/coder.json";

}

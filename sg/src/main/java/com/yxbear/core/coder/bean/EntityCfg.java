package com.yxbear.core.coder.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityCfg extends GraphNodeCfg {

    String modelContent;

}

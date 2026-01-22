package com.yxbear.core.pk.bean;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class PrimaryKey implements EntityBean<Long> {

    private Long id;

    private String name;

    private String value;

    private String version;

    private Long createTime;

    private Long modifyTime;

}

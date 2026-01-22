package com.yxbear.core.bean;

public interface EntityBean<I extends Number> {

    /**
     * 获取实体对象ID
     */
    I getId();

    /**
     * 设置实体对象ID
     *
     * @param id
     */
    void setId(I id);

}

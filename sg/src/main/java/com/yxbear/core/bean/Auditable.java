package com.yxbear.core.bean;

public interface Auditable {

    Long getCreateTime();

    Long getModifyTime();

    /**
     * 设置数据创建时间, 格式为yyyyMMddHHmmss
     *
     * @param createTime
     */
    void setCreateTime(Long createTime);

    /**
     * 设置数据更改时间, 格式为yyyyMMddHHmmss
     *
     * @param modifyTime
     */

    void setModifyTime(Long modifyTime);

}

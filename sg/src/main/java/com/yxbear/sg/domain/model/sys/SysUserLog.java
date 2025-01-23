package com.yxbear.sg.domain.model.sys;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class SysUserLog implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 1登录,2登出 */
    private Integer action;

    /** 内容 */
    private String remark;

    /** 最后登录Token! */
    private String token;

    /** 最后登录Ip! */
    private String ip;



}
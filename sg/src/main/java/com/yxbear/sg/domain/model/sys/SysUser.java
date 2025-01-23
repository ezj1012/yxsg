package com.yxbear.sg.domain.model.sys;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class SysUser implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 登录名称 */
    private String loginCode;

    /** 密码 */
    private String password;

    /** qq号 */
    private String qqNo;

    /** 1为管理员,0为普通用户 */
    private Integer adminFlag;

    /** 1为正常,2未激活,3锁定,0删除 */
    private Integer status;

    /** 最后登录时间! */
    private Long lastLoginTime;

    /** 最后登录Token! */
    private String lastToken;

    /** 最后登录Ip! */
    private String lastIp;

}
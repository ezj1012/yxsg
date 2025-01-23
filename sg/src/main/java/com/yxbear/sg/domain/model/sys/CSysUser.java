package com.yxbear.sg.domain.model.sys;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CSysUser implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private String loginCode;

    private String[] loginCodes;

    private String loginCodeEqual;

    private String password;

    private String[] passwords;

    private String passwordEqual;

    private String qqNo;

    private String[] qqNos;

    private String qqNoEqual;

    private Integer adminFlag;

    private Integer[] adminFlags;

    private Integer startAdminFlag;

    private Integer endAdminFlag;

    private Integer status;

    private Integer[] statuss;

    private Integer startStatus;

    private Integer endStatus;

    private Long lastLoginTime;

    private Long[] lastLoginTimes;

    private Long startLastLoginTime;

    private Long endLastLoginTime;

    private String lastToken;

    private String[] lastTokens;

    private String lastTokenEqual;

    private String lastIp;

    private String[] lastIps;

    private String lastIpEqual;

}
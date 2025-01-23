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
public class CSysUserLog implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer action;

    private Integer[] actions;

    private Integer startAction;

    private Integer endAction;

    private String remark;

    private String[] remarks;

    private String remarkEqual;

    private String token;

    private String[] tokens;

    private String tokenEqual;

    private String ip;

    private String[] ips;

    private String ipEqual;

}
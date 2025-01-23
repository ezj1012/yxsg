package com.yxbear.sg.domain.mapper.sys;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.sys.CSysUserLog;
import com.yxbear.sg.domain.model.sys.SysUserLog;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface SysUserLogMapper extends BaseMapper<Integer, SysUserLog, CSysUserLog> {

}
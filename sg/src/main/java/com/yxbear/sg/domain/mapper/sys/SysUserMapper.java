package com.yxbear.sg.domain.mapper.sys;

import org.apache.ibatis.annotations.Mapper;

import com.yxbear.sg.domain.model.sys.CSysUser;
import com.yxbear.sg.domain.model.sys.SysUser;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface SysUserMapper extends BaseMapper<Integer, SysUser, CSysUser> {

}